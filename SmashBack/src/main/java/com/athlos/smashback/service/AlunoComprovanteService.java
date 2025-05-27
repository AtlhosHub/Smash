package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.MensalidadeRepository;
import com.athlos.smashback.specification.AlunoSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlunoComprovanteService {

    private final AlunoRepository alunoRepository;
    private final MensalidadeRepository mensalidadeRepository;
    private static final List<Status> TODOS_STATUS =
            List.of(Status.PENDENTE, Status.ATRASADO, Status.PAGO);

    public AlunoComprovanteService(AlunoRepository alunoRepository,
                                   MensalidadeRepository mensalidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.mensalidadeRepository = mensalidadeRepository;
    }

    /**
     * Lista cada aluno com suas mensalidades, retornando para cada mensalidade:
     * - id, nome e ativo do aluno
     * - data de pagamento (dataEnvio da mensalidade)
     * - status da mensalidade
     * Aplica filtros de AlunoFilter sobre status e período de data de envio.
     */
    public List<AlunoComprovanteDTO> listarAlunosComComprovantes(AlunoFilter filtro) {
        Specification<Aluno> spec = AlunoSpecification.filtrarPor(filtro);
        List<Aluno> alunos = alunoRepository.findAll(Specification.where(spec), Sort.by(Sort.Order.asc("nome").ignoreCase()));
        if (alunos.isEmpty()) return Collections.emptyList();

        List<AlunoComprovanteDTO> resultado = new ArrayList<>();

        LocalDateTime from = filtro.getDataEnvioFrom() != null
                ? filtro.getDataEnvioFrom().atStartOfDay()
                : null;

        LocalDateTime to = filtro.getDataEnvioTo() != null
                ? filtro.getDataEnvioTo().atTime(23, 59, 59)
                : null;


        for (Aluno aluno : alunos) {
            List<Mensalidade> mensalidades = mensalidadeRepository
                    .findByAlunoAndStatusInOrderByDataVencimentoAsc(aluno, TODOS_STATUS);

            LocalDate fromDate = filtro.getDataEnvioFrom();
            LocalDate toDate   = filtro.getDataEnvioTo();

            for (Mensalidade m : mensalidades) {
                LocalDateTime dataPagamento = m.getDataPagamento();
                LocalDate dataVencimento = m.getDataVencimento();
                String      status         = m.getStatus().name();


                if (filtro.getStatus() != null && !status.equalsIgnoreCase(filtro.getStatus())) {
                    continue;
                }

                if (fromDate != null && dataVencimento.isBefore(fromDate)) {
                    continue;
                }
                if (toDate   != null && dataVencimento.isAfter(toDate)) {
                    continue;
                }

                resultado.add(new AlunoComprovanteDTO(
                        m.getId(),
                        aluno.getId(),
                        (aluno.getNomeSocial().trim().isEmpty() || aluno.getNomeSocial() == null) ? aluno.getNome() : aluno.getNomeSocial(),
                        aluno.isAtivo(),
                        dataPagamento,
                        m.getDataVencimento(),
                        status,
                        m.getFormaPagamento(),
                        m.getStatus() == Status.PAGO ?  m.getValor().getValor() : null,
                        m.getValor().isDesconto()
                ));
            }
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<AlunoComprovanteDTO> buscarMensalidadesAteMesAtual(
            int alunoId,
            LocalDate dateFrom,
            LocalDate dateTo) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Aluno não encontrado com id " + alunoId));

        LocalDate fimMesAtual = YearMonth.now().atEndOfMonth();

        return mensalidadeRepository.findByAluno(aluno).stream()
                .filter(m ->
                        !m.getDataVencimento().isAfter(fimMesAtual) ||
                                m.getStatus() == Status.PAGO
                )
                .filter(m -> {
                    if (dateFrom != null && dateTo != null) {
                        LocalDate venc = m.getDataVencimento();
                        return !venc.isBefore(dateFrom) && !venc.isAfter(dateTo);
                    }
                    return true;
                })
                .sorted(Comparator.comparing(Mensalidade::getDataVencimento).reversed())
                .map(m -> new AlunoComprovanteDTO(
                        m.getId(),
                        aluno.getId(),
                        (aluno.getNomeSocial() == null || aluno.getNomeSocial().trim().isEmpty()) ? aluno.getNome() : aluno.getNomeSocial(),
                        aluno.isAtivo(),
                        m.getDataPagamento(),
                        m.getDataVencimento(),
                        m.getStatus().name(),
                        m.getFormaPagamento(),
                        m.getStatus() == Status.PAGO ?  m.getValor().getValor() : null,
                        m.getValor().isDesconto()
                ))
                .collect(Collectors.toList());
    }
}

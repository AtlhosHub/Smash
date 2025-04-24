package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.MensalidadeRepository;
import com.athlos.smashback.specification.AlunoSpecification;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
        // 1. Filtra alunos
        Specification<Aluno> spec = AlunoSpecification.filtrarPor(filtro);
        List<Aluno> alunos = alunoRepository.findAll(spec);
        if (alunos.isEmpty()) return Collections.emptyList();

        List<AlunoComprovanteDTO> resultado = new ArrayList<>();

        LocalDateTime from = filtro.getDataEnvioFrom() != null
                ? filtro.getDataEnvioFrom().atStartOfDay()
                : null;

        LocalDateTime to = filtro.getDataEnvioTo() != null
                ? filtro.getDataEnvioTo().atTime(23, 59, 59)
                : null;


        // 2. Para cada aluno, busca suas mensalidades com qualquer status
        for (Aluno aluno : alunos) {
            List<Mensalidade> mensalidades = mensalidadeRepository
                    .findByAlunoAndStatusInOrderByDataVencimentoAsc(aluno, TODOS_STATUS);

            LocalDate fromDate = filtro.getDataEnvioFrom();
            LocalDate toDate   = filtro.getDataEnvioTo();

            for (Mensalidade m : mensalidades) {
                LocalDateTime dataPagamento = m.getDataPagamento();
                LocalDate dataVencimento = m.getDataVencimento();
                String      status         = m.getStatus().name();


                // 3. Filtra por status, se informado
                if (filtro.getStatus() != null && !status.equalsIgnoreCase(filtro.getStatus())) {
                    continue;
                }

                if (fromDate != null && dataVencimento.isBefore(fromDate)) {
                    continue;
                }
                if (toDate   != null && dataVencimento.isAfter(toDate)) {
                    continue;
                }

                // 6. Adiciona ao resultado, observando ordem id, nome, ativo, dataEnvio, status
                resultado.add(new AlunoComprovanteDTO(
                        aluno.getId(),
                        aluno.getNome(),
                        aluno.isAtivo(),
                        dataPagamento,
                        status
                ));
            }
        }
        return resultado;
    }
}

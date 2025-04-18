package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Comprovante;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.ComprovanteRepository;
import com.athlos.smashback.specification.AlunoSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlunoComprovanteService {

    private final AlunoRepository alunoRepository;
    private final ComprovanteRepository comprovanteRepository;
    public AlunoComprovanteService(AlunoRepository alunoRepository, ComprovanteRepository comprovanteRepository) {
        this.alunoRepository = alunoRepository;
        this.comprovanteRepository = comprovanteRepository;
    }

    public List<AlunoComprovanteDTO> listarAlunosComComprovantes(AlunoFilter filtro) {
        Specification<Aluno> spec = AlunoSpecification.filtrarPor(filtro);
        List<Aluno> alunos = alunoRepository.findAll(spec);

        YearMonth mesAtual = YearMonth.now();
        YearMonth mesAnterior = mesAtual.minusMonths(1);

        List<Integer> idsAlunos = alunos.stream().map(Aluno::getId).toList();

        List<Comprovante> comprovantesMesAtual = comprovanteRepository.findByAlunoIdInAndMes(
                idsAlunos, mesAtual.getYear(), mesAtual.getMonthValue()
        );

        List<Comprovante> comprovantesMesAnterior = comprovanteRepository.findByAlunoIdInAndMes(
                idsAlunos, mesAnterior.getYear(), mesAnterior.getMonthValue()
        );

        Map<Integer, List<Comprovante>> comprovantesAtualMap = comprovantesMesAtual.stream().collect(Collectors.groupingBy(c -> c.getAluno().getId()));

        Map<Integer, List<Comprovante>> comprovantesAnteriorMap = comprovantesMesAnterior.stream().collect(Collectors.groupingBy(c -> c.getAluno().getId()));

        List<AlunoComprovanteDTO> resultado = new ArrayList<>();

        for (Aluno aluno : alunos) {
            List<Comprovante> atual = comprovantesAtualMap.getOrDefault(aluno.getId(), Collections.emptyList());
            List<Comprovante> anterior = comprovantesAnteriorMap.getOrDefault(aluno.getId(), Collections.emptyList());

            String status;
            LocalDateTime dataEnvio = null;

            if (!atual.isEmpty()) {
                dataEnvio = comprovantesMesAtual.getFirst().getDataEnvio();
                status = "ENVIADO";
            } else if (!anterior.isEmpty()) {
                status = "PENDENTE";
            } else {
                status = "EM ATRASO";
            }

            if (filtro.getStatus() != null && !status.equals(filtro.getStatus())) continue;
            if (filtro.getDataEnvioFrom() != null && (dataEnvio == null || dataEnvio.toLocalDate().isBefore(filtro.getDataEnvioFrom()))) continue;
            if (filtro.getDataEnvioTo() != null && (dataEnvio == null || dataEnvio.toLocalDate().isAfter(filtro.getDataEnvioTo()))) continue;

            resultado.add(new AlunoComprovanteDTO(
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.isAtivo(),
                    dataEnvio,
                    status
            ));
        }

        return resultado;
    }
}


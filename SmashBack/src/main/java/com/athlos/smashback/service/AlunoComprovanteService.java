package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Comprovante;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.ComprovanteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlunoComprovanteService {

    private AlunoRepository alunoRepository;
    private ComprovanteRepository comprovanteRepository;
    public AlunoComprovanteService(AlunoRepository alunoRepository, ComprovanteRepository comprovanteRepository) {
        this.alunoRepository = alunoRepository;
        this.comprovanteRepository = comprovanteRepository;
    }

    public List<AlunoComprovanteDTO> buscarAlunosComComprovantes() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoComprovanteDTO> resultado = new ArrayList<>();

        YearMonth mesAtual = YearMonth.now();
        YearMonth mesPassado = mesAtual.minusMonths(1);

        for (Aluno aluno : alunos) {
            List<Comprovante> comprovantesMesAtual = comprovanteRepository.findByAlunoAndMonth(aluno.getId(), mesAtual.getYear(), mesAtual.getMonthValue());
            List<Comprovante> comprovantesMesPassado = comprovanteRepository.findByAlunoAndMonth(aluno.getId(), mesPassado.getYear(), mesPassado.getMonthValue());

            String status;
            LocalDateTime dataEnvio = null;

            if (!comprovantesMesAtual.isEmpty()) {
                status = "ENVIADO";
                dataEnvio = comprovantesMesAtual.get(0).getDataEnvio();
            } else if (!comprovantesMesPassado.isEmpty()) {
                status = "NÃO ENVIADO";
            } else {
                status = "EM ATRASO";
            }

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


package com.athlos.smashback.service;

import com.athlos.smashback.dto.ConfiguracoesHorario;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.HorarioPrefRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioPrefService {
    private final HorarioPrefRepository horarioPrefRepository;
    public HorarioPrefService(HorarioPrefRepository horarioPrefRepository) {
        this.horarioPrefRepository = horarioPrefRepository;
    }

    public ResponseEntity<List<HorarioPref>> listarHorarios() {
        List<HorarioPref> horarios = horarioPrefRepository.findAll();
        return ResponseEntity.ok(horarios.isEmpty() ? List.of() : horarios);
    }

    public ResponseEntity<HorarioPref> buscarHorario(int id) {
        if(!horarioPrefRepository.existsById(id)){
            throw new ResourceNotFoundException("Horário não encontrado");
        }

        return ResponseEntity.ok(horarioPrefRepository.findById(id).get());
    }

    public ResponseEntity<HorarioPref> cadastrarHorario(HorarioPref horarioPref) {
        if(horarioPrefRepository.existsByHorarioAulaInicioAndHorarioAulaFim(horarioPref.getHorarioAulaInicio(), horarioPref.getHorarioAulaFim())) {
            throw new DataConflictException("Horário já cadastrado");
        }

        return ResponseEntity.ok(horarioPrefRepository.save(horarioPref));
    }

    public ResponseEntity<Void> deletarHorario(int id) {
        if(horarioPrefRepository.existsById(id)) {
            HorarioPref horario = horarioPrefRepository.findById(id).get();
            for (ListaEspera interessado : horario.getInteressados()) {
                interessado.setHorarioPref(null);
            }

            horarioPrefRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("Horário não encontrado");
    }

    public ResponseEntity<HorarioPref> atualizarHorario(int id, HorarioPref novoHorario) {
        if(horarioPrefRepository.existsByHorarioAulaInicioAndHorarioAulaFimAndIdIsNot(novoHorario.getHorarioAulaInicio(), novoHorario.getHorarioAulaFim(), id)) {
            throw new DataConflictException("Horário já cadastrado");
        }

        return horarioPrefRepository.findById(id).map(horario -> {
            horario.setHorarioAulaInicio(novoHorario.getHorarioAulaInicio());
            horario.setHorarioAulaFim(novoHorario.getHorarioAulaFim());
            return ResponseEntity.ok(horarioPrefRepository.save(horario));
        }).orElseThrow(() -> new ResourceNotFoundException("Horário não encontrado"));
    }

    public ResponseEntity<List<HorarioPref>> adicionarOuAtualizarHorarios(List<ConfiguracoesHorario> horarios) {
        List<HorarioPref> horariosCadastradosOuAtualizados = new ArrayList<>();
        for (ConfiguracoesHorario horario : horarios) {
            if(horario.getId() != null){
                if(horarioPrefRepository.existsById(horario.getId())) {
                    HorarioPref horarioExistente = horarioPrefRepository.findById(horario.getId()).get();
                    horarioExistente.setHorarioAulaInicio(horario.getHorarioAulaInicio());
                    horarioExistente.setHorarioAulaFim(horario.getHorarioAulaFim());
                    horariosCadastradosOuAtualizados.add(horarioPrefRepository.save(horarioExistente));
                } else {
                    throw new ResourceNotFoundException("Horário não encontrado");
                }
            } else {
                if(horarioPrefRepository.existsByHorarioAulaInicioAndHorarioAulaFim(horario.getHorarioAulaInicio(), horario.getHorarioAulaFim())) {
                    throw new DataConflictException("Horário já cadastrado");
                }
                HorarioPref novoHorario = new HorarioPref();
                novoHorario.setHorarioAulaInicio(horario.getHorarioAulaInicio());
                novoHorario.setHorarioAulaFim(horario.getHorarioAulaFim());
                horariosCadastradosOuAtualizados.add(horarioPrefRepository.save(novoHorario));
            }
        }
        return ResponseEntity.ok(horariosCadastradosOuAtualizados.isEmpty() ? List.of() : horariosCadastradosOuAtualizados);
    }
}

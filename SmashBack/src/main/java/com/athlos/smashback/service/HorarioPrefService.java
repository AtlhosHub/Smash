package com.athlos.smashback.service;

import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.repository.HorarioPrefRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioPrefService {
    private final HorarioPrefRepository horarioPrefRepository;
    public HorarioPrefService(HorarioPrefRepository horarioPrefRepository) {
        this.horarioPrefRepository = horarioPrefRepository;
    }

    public ResponseEntity<List<HorarioPref>> listarHorarios() {
        List<HorarioPref> horarios = horarioPrefRepository.findAll();
        return horarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(horarios);
    }

    public ResponseEntity<HorarioPref> cadastrarHorario(HorarioPref horarioPref) {
        if(horarioPrefRepository.existsByHorarioAula(horarioPref.getHorarioAula())) {
            return ResponseEntity.status(409).body(horarioPref);
        }

        return ResponseEntity.ok(horarioPrefRepository.save(horarioPref));
    }

    public ResponseEntity<Void> deletarHorario(int id) {
        if(horarioPrefRepository.existsById(id)) {
            horarioPrefRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<HorarioPref> atualizarHorario(int id, HorarioPref novoHorario) {
        if(horarioPrefRepository.existsByHorarioAulaAndIdIsNot(novoHorario.getHorarioAula(), id)) {
            return ResponseEntity.status(409).body(novoHorario);
        }

        return horarioPrefRepository.findById(id).map(horario -> {
            horario.setHorarioAula(novoHorario.getHorarioAula());
            return ResponseEntity.ok(horarioPrefRepository.save(horario));
        }).orElse(ResponseEntity.notFound().build());
    }
}

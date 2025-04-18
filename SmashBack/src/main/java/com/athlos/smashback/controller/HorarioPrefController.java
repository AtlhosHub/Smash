package com.athlos.smashback.controller;

import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.service.HorarioPrefService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario-pref")
public class HorarioPrefController {
    private final HorarioPrefService horarioPrefService;
    public HorarioPrefController(HorarioPrefService horarioPrefService) {
        this.horarioPrefService = horarioPrefService;
    }

    @GetMapping
    public ResponseEntity<List<HorarioPref>> listarHorarios() {
        return horarioPrefService.listarHorarios();
    }

    @PostMapping
    public ResponseEntity<HorarioPref> cadastrarHorario(@RequestBody HorarioPref horarioPref) {
        return horarioPrefService.cadastrarHorario(horarioPref);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHorario(@PathVariable int id) {
        return horarioPrefService.deletarHorario(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioPref> atualizarHorario(@PathVariable int id, @RequestBody HorarioPref novoHorario) {
        return horarioPrefService.atualizarHorario(id, novoHorario);
    }
}

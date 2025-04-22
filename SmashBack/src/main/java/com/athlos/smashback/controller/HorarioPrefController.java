package com.athlos.smashback.controller;

import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.service.HorarioPrefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario-pref")
@SecurityRequirement(name = "Bearer")
@Tag(name = "HorarioPrefController", description = "Endpoints para gerenciar horários de aulas no sistema")
public class HorarioPrefController {
    private final HorarioPrefService horarioPrefService;

    public HorarioPrefController(HorarioPrefService horarioPrefService) {
        this.horarioPrefService = horarioPrefService;
    }

    @GetMapping
    @Operation(summary = "Listar horários de aula", description = "Retorna uma lista de todos os horários de aula cadastrados no sistema.")
    public ResponseEntity<List<HorarioPref>> listarHorarios() {
        return horarioPrefService.listarHorarios();
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo horário de aula", description = "Adiciona um novo horário de aula ao sistema.")
    public ResponseEntity<HorarioPref> cadastrarHorario(
            @RequestBody @Parameter(description = "Dados do horário de aula a ser cadastrado") HorarioPref horarioPref) {
        return horarioPrefService.cadastrarHorario(horarioPref);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar horário de aula por ID", description = "Remove um horário de aula do sistema com base no ID fornecido.")
    public ResponseEntity<Void> deletarHorario(
            @Parameter(description = "ID do horário de aula a ser deletado", example = "1") @PathVariable int id) {
        return horarioPrefService.deletarHorario(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar horário de aula por ID", description = "Atualiza os dados de um horário de aula existente com base no ID fornecido.")
    public ResponseEntity<HorarioPref> atualizarHorario(
            @Parameter(description = "ID do horário de aula a ser atualizado", example = "1") @PathVariable int id,
            @RequestBody @Parameter(description = "Novos dados do horário de aula") HorarioPref novoHorario) {
        return horarioPrefService.atualizarHorario(id, novoHorario);
    }
}

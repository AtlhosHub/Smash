package com.athlos.smashback.controller;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@SecurityRequirement(name = "Bearer")
@Tag(name = "AlunoController", description = "Endpoints para gerenciar os alunos no sistema")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados no sistema.")
    public ResponseEntity<List<Aluno>> listarAlunos() {
        return alunoService.listarAlunos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os detalhes de um aluno específico com base no ID fornecido.")
    public ResponseEntity<Aluno> buscarAluno(
            @Parameter(description = "ID do aluno a ser buscado", example = "1") @PathVariable int id) {
        return alunoService.buscarAluno(id);
    }

    @PostMapping("/comprovantes")
    @Operation(summary = "Listar alunos com comprovantes", description = "Retorna uma lista de alunos que possuem comprovantes com base no filtro fornecido.")
    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes(
            @RequestBody AlunoFilter filtro) {
        return alunoService.listarAlunosComComprovantes(filtro);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo aluno", description = "Adiciona um novo aluno ao sistema.")
    public ResponseEntity<Aluno> cadastrarAluno(
            @Valid @RequestBody Aluno aluno) {
        return alunoService.cadastrarAluno(aluno);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar aluno por ID", description = "Remove um aluno do sistema com base no ID fornecido.")
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID do aluno a ser deletado", example = "1") @PathVariable int id) {
        return alunoService.deletarAluno(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno por ID", description = "Atualiza os dados de um aluno existente com base no ID fornecido.")
    public ResponseEntity<Aluno> atualizarAluno(
            @Parameter(description = "ID do aluno a ser atualizado", example = "1") @PathVariable int id,
            @Valid @RequestBody Aluno novoAluno) {
        return alunoService.atualizarAluno(id, novoAluno);
    }
}
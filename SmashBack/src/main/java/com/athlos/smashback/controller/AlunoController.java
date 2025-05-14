package com.athlos.smashback.controller;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.service.AlunoComprovanteService;
import com.athlos.smashback.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final AlunoComprovanteService alunoComprovanteService;

    public AlunoController(AlunoService alunoService, AlunoComprovanteService alunoComprovanteService) {
        this.alunoService = alunoService;
        this.alunoComprovanteService = alunoComprovanteService;
    }
    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Lista de alunos vazia", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<Aluno>> listarAlunos() {
        return alunoService.listarAlunos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os detalhes de um aluno específico com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do aluno retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Aluno> buscarAluno(
            @Parameter(description = "ID do aluno a ser buscado", example = "1") @PathVariable int id) {
        return alunoService.buscarAluno(id);
    }

    @PostMapping("/comprovantes")
    @Operation(summary = "Listar alunos com comprovantes", description = "Retorna uma lista de alunos que possuem comprovantes com base no filtro fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso"),
//            @ApiResponse(responseCode = "204", description = "Lista de alunos vazia", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes(
            @RequestBody AlunoFilter filtro) {
        return alunoService.listarAlunosComComprovantes(filtro);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo aluno", description = "Adiciona um novo aluno ao sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do aluno não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "409", description = "RG, CPF ou e-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Aluno> cadastrarAluno(
            @Valid @RequestBody Aluno aluno) {
        return alunoService.cadastrarAluno(aluno);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar aluno por ID", description = "Remove um aluno do sistema com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID do aluno a ser deletado", example = "1") @PathVariable int id) {
        return alunoService.deletarAluno(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno por ID", description = "Atualiza os dados de um aluno existente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do aluno não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "RG, CPF ou e-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Aluno> atualizarAluno(
            @Parameter(description = "ID do aluno a ser atualizado", example = "1") @PathVariable int id,
            @Valid @RequestBody Aluno novoAluno) {
        return alunoService.atualizarAluno(id, novoAluno);
    }

    @GetMapping("/{id}/historicoMensalidade")
    @Operation(summary = "Listar mensalidades até o mês atual", description = "Retorna as mensalidades de um aluno até o mês atual (inclusive).")
    public ResponseEntity<List<AlunoComprovanteDTO>> listarMensalidadesAteAgora(
            @Parameter(description = "ID do aluno a ser procurado", example = "1") @PathVariable int id
            ) {
        List<AlunoComprovanteDTO> mensalidades =
                alunoComprovanteService.buscarMensalidadesAteMesAtual(id);
        return ResponseEntity.ok(mensalidades);
    }
}
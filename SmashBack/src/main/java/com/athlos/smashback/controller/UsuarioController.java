package com.athlos.smashback.controller;

import com.athlos.smashback.dto.*;
import com.athlos.smashback.filter.UsuarioFilter;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.service.UsuarioService;
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
@RequestMapping("/usuarios")
@Tag(name = "UsuarioController", description = "Endpoints para gerenciar usuários no sistema")
public class UsuarioController {
    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping

    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Lista de usuários vazia", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/filtro")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Filtrar usuários", description = "Retorna uma lista de usuários com base nos critérios do filtro fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Lista de usuários vazia", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<UsuarioListaDTO>> usuarioFiltro(
            @RequestBody @Parameter(description = "Critérios para filtrar os usuários") UsuarioFilter filtro) {
        return usuarioService.usuarioFiltro(filtro);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário a ser buscado", example = "1") @PathVariable int id) {

        return usuarioService.buscarUsuarioPorId(id);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Adicionar um novo usuário", description = "Adiciona um novo usuário ao sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do usuário não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Usuario> adicionarUsuario(
            @Valid @RequestBody @Parameter(description = "Dados do usuário a ser adicionado") Usuario usuario) {

        return usuarioService.adicionarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Remover usuário por ID", description = "Remove um usuário do sistema com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Void> removerUsuario(
            @Parameter(description = "ID do usuário a ser removido", example = "1") @PathVariable int id) {

        return usuarioService.removerUsuario(id);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Atualizar usuário por ID", description = "Atualiza os dados de um usuário existente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do usuário não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Usuario> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado", example = "1") @PathVariable int id,
            @Valid @RequestBody @Parameter(description = "Novos dados do usuário") Usuario novoUsuario) {
        return usuarioService.atualizarUsuario(id, novoUsuario);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação de um usuário e retorna um token de acesso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "E-mail do usuário não cadastrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<UsuarioTokenDTO> login(
            @RequestBody @Parameter(description = "Credenciais do usuário para autenticação") UsuarioLoginDTO usuarioLoginDTO) {
        final Usuario usuario = UsuarioMapper.of(usuarioLoginDTO);
        UsuarioTokenDTO usuarioTokenDTO = usuarioService.autenticar(usuario);
        return ResponseEntity.ok(usuarioTokenDTO);
    }

    @GetMapping("/vazio")
    public ResponseEntity<Void> retornarVazio() {
        return ResponseEntity.noContent().build();
    }
}

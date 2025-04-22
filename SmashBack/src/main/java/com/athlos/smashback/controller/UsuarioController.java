package com.athlos.smashback.controller;

import com.athlos.smashback.dto.*;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "UsuarioController", description = "Endpoints para gerenciar usuários no sistema")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.")
    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico com base no ID fornecido.")
    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário a ser buscado", example = "1") @PathVariable int id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Adicionar um novo usuário", description = "Adiciona um novo usuário ao sistema.")
    public ResponseEntity<Usuario> adicionarUsuario(
            @Valid @RequestBody @Parameter(description = "Dados do usuário a ser adicionado") Usuario usuario) {
        return usuarioService.adicionarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Remover usuário por ID", description = "Remove um usuário do sistema com base no ID fornecido.")
    public ResponseEntity<Void> removerUsuario(
            @Parameter(description = "ID do usuário a ser removido", example = "1") @PathVariable int id) {
        return usuarioService.removerUsuario(id);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Atualizar usuário por ID", description = "Atualiza os dados de um usuário existente com base no ID fornecido.")
    public ResponseEntity<Usuario> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado", example = "1") @PathVariable int id,
            @Valid @RequestBody @Parameter(description = "Novos dados do usuário") Usuario novoUsuario) {
        return usuarioService.atualizarUsuario(id, novoUsuario);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação de um usuário e retorna um token de acesso.")
    public ResponseEntity<UsuarioTokenDTO> login(
            @RequestBody @Parameter(description = "Credenciais do usuário para autenticação") UsuarioLoginDTO usuarioLoginDTO) {
        final Usuario usuario = UsuarioMapper.of(usuarioLoginDTO);
        UsuarioTokenDTO usuarioTokenDTO = usuarioService.autenticar(usuario);
        return ResponseEntity.ok(usuarioTokenDTO);
    }
}

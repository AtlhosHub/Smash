package com.athlos.smashback.controller;

import com.athlos.smashback.dto.*;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(@PathVariable int id){
        return usuarioService.buscarUsuarioPorId(id);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Usuario> adicionarUsuario(@Valid @RequestBody Usuario usuario){
        return usuarioService.adicionarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> removerUsuario(@PathVariable int id){
        return usuarioService.removerUsuario(id);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @Valid @RequestBody Usuario novoUsuario){
        return usuarioService.atualizarUsuario(id, novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDTO> login(@RequestBody UsuarioLoginDTO usuarioLoginDTO){
        final Usuario usuario = UsuarioMapper.of(usuarioLoginDTO);
        UsuarioTokenDTO usuarioTokenDTO = usuarioService.autenticar(usuario);
        return ResponseEntity.ok(usuarioTokenDTO);
    }
}

package com.athlos.smashback.controller;

import com.athlos.smashback.dto.UsuarioInfoDTO;
import com.athlos.smashback.dto.UsuarioListaDTO;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(@PathVariable int id){
        return usuarioService.buscarUsuarioPorId(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> adicionarUsuario(@Valid @RequestBody Usuario usuario){
        return usuarioService.adicionarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable int id){
        return usuarioService.removerUsuario(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @Valid @RequestBody Usuario novoUsuario){
        return usuarioService.atualizarUsuario(id, novoUsuario);
    }

    @GetMapping("/vazio")
    public ResponseEntity<Void> retornarVazio() {
        return ResponseEntity.noContent().build();
    }
}

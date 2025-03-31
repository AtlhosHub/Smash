package com.athlos.smashback.controller;

import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.service.UsuarioService;
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
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id){
        return usuarioService.buscarUsuarioPorId(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> adicionarUsuario(@RequestBody Usuario usuario){
        return usuarioService.adicionarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable int id){
        return usuarioService.removerUsuario(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @RequestBody Usuario novoUsuario){
        return usuarioService.atualizarUsuario(id, novoUsuario);
    }
}

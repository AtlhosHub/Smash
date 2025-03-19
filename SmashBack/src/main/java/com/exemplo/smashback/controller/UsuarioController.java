package com.exemplo.smashback.controller;

import com.exemplo.smashback.model.Usuario;
import com.exemplo.smashback.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAllByDeletado(false);
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id){
        return usuarioRepository.existsById(id) ? ResponseEntity.ok(usuarioRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Usuario> adicionarUsuario(@RequestBody Usuario usuario){
        return usuarioRepository.existsByEmail(usuario.getEmail()) ? ResponseEntity.status(409).body(usuario) : ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable int id){
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable int id, @RequestBody Usuario novoUsuario){
        if(usuarioRepository.existsByEmailAndIdIsNot(novoUsuario.getEmail(),id)){
            return ResponseEntity.status(409).body(novoUsuario);
        }
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(novoUsuario.getNome());
            usuario.setEmail(novoUsuario.getEmail());
            usuario.setCelular(novoUsuario.getCelular());
            usuario.setDataNascimento(novoUsuario.getDataNascimento());
            usuario.setSenha(novoUsuario.getSenha());
            usuario.setCargo(novoUsuario.getCargo());
            usuario.setDeletado(novoUsuario.isDeletado());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

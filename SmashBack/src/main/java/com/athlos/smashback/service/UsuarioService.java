package com.athlos.smashback.service;

import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAllByDeletado(false);
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    public ResponseEntity<Usuario> buscarUsuarioPorId(int id) {
        return usuarioRepository.existsById(id) ? ResponseEntity.ok(usuarioRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    public ResponseEntity<Usuario> adicionarUsuario(Usuario usuario) {
        return usuarioRepository.existsByEmail(usuario.getEmail()) ? ResponseEntity.status(409).body(usuario) : ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    public ResponseEntity<Void> removerUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Usuario> atualizarUsuario(int id, Usuario novoUsuario) {
        if (usuarioRepository.existsByEmailAndIdIsNot(novoUsuario.getEmail(), id)) {
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

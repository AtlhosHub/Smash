package com.athlos.smashback.service;

import com.athlos.smashback.config.GerenciadorTokenJWT;
import com.athlos.smashback.dto.UsuarioInfoDTO;
import com.athlos.smashback.dto.UsuarioListaDTO;
import com.athlos.smashback.dto.UsuarioMapper;
import com.athlos.smashback.dto.UsuarioTokenDTO;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GerenciadorTokenJWT gerenciadorTokenJWT;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UsuarioTokenDTO autenticar(Usuario usuario) {
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(() -> new ResponseStatusException(404, "Email do usuário não cadastrado", null));
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
        final Authentication authentication = authenticationManager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJWT.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioListaDTO> usuariosLista = usuarios.stream().map(usuario -> new UsuarioListaDTO(usuario.getId(), usuario.getNome())).toList();

        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuariosLista);
    }

    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(int id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioRepository.findById(id).get();
        UsuarioInfoDTO dadosUsuario = new UsuarioInfoDTO(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getDataNascimento(), usuario.getNomeSocial(), usuario.getGenero(), usuario.getTelefone(), usuario.getCargo());

        return ResponseEntity.ok(dadosUsuario);
    }

    public ResponseEntity<Usuario> adicionarUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
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
            usuario.setNomeSocial(novoUsuario.getNomeSocial());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

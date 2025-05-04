package com.athlos.smashback.service;

import com.athlos.smashback.config.GerenciadorTokenJWT;
import com.athlos.smashback.dto.*;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.UsuarioFilter;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import com.athlos.smashback.specification.UsuarioSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        Usuario usuarioAutenticado = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email do usuário não cadastrado"));
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
        final Authentication authentication = authenticationManager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJWT.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    public ResponseEntity<List<UsuarioListaDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
        List<UsuarioListaDTO> usuariosLista = usuarios.stream().map(usuario -> new UsuarioListaDTO(usuario.getId(), usuario.getNome())).toList();

        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuariosLista);
    }

    public ResponseEntity<List<UsuarioListaDTO>> usuarioFiltro(UsuarioFilter filtro){
        Specification<Usuario> spec = UsuarioSpecification.filtrarPor(filtro);
        List<Usuario> usuarios = usuarioRepository.findAll(Specification.where(spec), Sort.by(Sort.Direction.ASC, "nome"));

        List<UsuarioListaDTO> usuariosLista = usuarios.stream().map(usuario -> new UsuarioListaDTO(usuario.getId(), usuario.getNome())).toList();
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuariosLista);
    }

    public ResponseEntity<UsuarioInfoDTO> buscarUsuarioPorId(int id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        Usuario usuario = usuarioRepository.findById(id).get();
        UsuarioInfoDTO dadosUsuario = new UsuarioInfoDTO(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getDataNascimento(), usuario.getNomeSocial(), usuario.getGenero(), usuario.getTelefone(), usuario.getCargo());

        return ResponseEntity.ok(dadosUsuario);
    }

    public ResponseEntity<UsuarioInfoDTO> adicionarUsuario(Usuario usuario) {
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new DataConflictException("E-mail de usuário já cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);

        UsuarioInfoDTO dadosUsuario = new UsuarioInfoDTO(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getDataNascimento(), usuario.getNomeSocial(), usuario.getGenero(), usuario.getTelefone(), usuario.getCargo());

        return ResponseEntity.ok(dadosUsuario);
    }

    public ResponseEntity<Void> removerUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("Usuário não encontrado");
    }

    public ResponseEntity<UsuarioInfoDTO> atualizarUsuario(int id, Usuario novoUsuario) {
        if (usuarioRepository.existsByEmailAndIdIsNot(novoUsuario.getEmail(), id)) {
            throw new DataConflictException("E-mail de usuário já cadastrado");
        }

        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(novoUsuario.getNome());
            usuario.setEmail(novoUsuario.getEmail());
            usuario.setCelular(novoUsuario.getCelular());
            usuario.setDataNascimento(novoUsuario.getDataNascimento());
            usuario.setCargo(novoUsuario.getCargo());
            usuario.setDeletado(novoUsuario.isDeletado());
            usuario.setNomeSocial(novoUsuario.getNomeSocial());
            usuario.setGenero(novoUsuario.getGenero());
            usuario.setTelefone(novoUsuario.getTelefone());

            usuarioRepository.save(usuario);

            UsuarioInfoDTO dadosUsuario = new UsuarioInfoDTO(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getDataNascimento(), usuario.getNomeSocial(), usuario.getGenero(), usuario.getTelefone(), usuario.getCargo());

            return ResponseEntity.ok(dadosUsuario);
        }).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}

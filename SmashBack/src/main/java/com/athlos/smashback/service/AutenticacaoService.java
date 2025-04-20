package com.athlos.smashback.service;

import com.athlos.smashback.dto.UsuarioDetalhesDTO;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);
        if(usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new UsuarioDetalhesDTO(usuarioOpt.get());
    }
}

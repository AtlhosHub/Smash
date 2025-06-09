package com.athlos.smashback.service;

import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AutenticacaoServiceTests {
    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        autenticacaoService = new AutenticacaoService(usuarioRepository);
    }

    @Test
    void loadUserByUsernameSuccess(){
        Usuario u = new Usuario();
        u.setEmail("user@email.com");
        when(usuarioRepository.findByEmailIgnoreCase(u.getEmail())).thenReturn(Optional.of(u));

        UserDetails userDetails = autenticacaoService.loadUserByUsername(u.getEmail());

        assertEquals("user@email.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameNotFound(){
        Usuario u = new Usuario();
        u.setEmail("notfound@email.com");
        when(usuarioRepository.findByEmailIgnoreCase(u.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            autenticacaoService.loadUserByUsername(u.getEmail());
        });
    }
}

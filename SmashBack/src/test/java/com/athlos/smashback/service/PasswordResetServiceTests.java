package com.athlos.smashback.service;

import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.PasswordResetToken;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.PasswordResetTokenRepository;
import com.athlos.smashback.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PasswordResetServiceTests {
    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepo;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordResetService = new PasswordResetService(usuarioRepository, tokenRepo, mailSender, passwordEncoder);
    }

    @Test
    void solicitarTokenSuccess() {
        long tokenExpirationMinutes = 60;

        String email = "user@email.com";
        Usuario u = new Usuario();
        u.setId(1);

        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(u));
        when(tokenRepo.findAllByUsuarioAndUsadoFalse(u)).thenReturn(List.of());

        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        PasswordResetToken t = new PasswordResetToken();
        t.setUsuario(u);
        t.setToken(UUID.randomUUID().toString());
        t.setExpiracao(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));

        passwordResetService.solicitarToken(email);

        verify(usuarioRepository).findByEmailIgnoreCase(email);
        verify(tokenRepo).findAllByUsuarioAndUsadoFalse(u);
        verify(tokenRepo).save(any(PasswordResetToken.class));
        verify(mailSender).send(emailCaptor.capture());

        SimpleMailMessage msg = emailCaptor.getValue();
        assertTrue(msg.getText().contains("Clique no link abaixo para redefinir sua senha"));
    }

    @Test
    void solicitarTokenNotFound() {
        String email = "notfound@email.com";

        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            passwordResetService.solicitarToken(email);
        });

        assertEquals("E-mail não cadastrado", exception.getMessage());
    }
}

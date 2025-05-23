package com.athlos.smashback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.athlos.smashback.model.PasswordResetToken;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.PasswordResetTokenRepository;
import com.athlos.smashback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    @Value("${app.reset-token.expiration-minutes:60}")
    private long tokenExpirationMinutes;

    @Value("${app.frontend.reset-url}")
    private String resetUrlBase;

    public PasswordResetService(UsuarioRepository usuarioRepo,
                                PasswordResetTokenRepository tokenRepo,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.tokenRepo = tokenRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gera e envia token; lança exceção se e-mail não existir
     */
    @Transactional
    public void solicitarToken(String email) {
        log.debug(">>> solicitarToken → email bruto:'{}' → trim:'{}'", email, email.trim());
        Usuario user = usuarioRepo.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NO_CONTENT, "E-mail não cadastrado")
                );
        tokenRepo.findAllByUsuarioAndUsadoFalse(user)
                .forEach(t -> t.setUsado(true));

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUsuario(user);
        prt.setToken(UUID.randomUUID().toString());
        prt.setExpiracao(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));
        tokenRepo.save(prt);

        String encodedToken = URLEncoder.encode(prt.getToken(), StandardCharsets.UTF_8);
        String link = resetUrlBase + "?token=" + encodedToken;

        String body = String.format(
                "Olá %s,%n%n" +
                        "Clique no link abaixo para redefinir sua senha:%n%s%n%n" +
                        "Ou use este código caso prefira:%n%s%n%n" +
                        "Esse link expira em %d minutos.",
                user.getNome(), link, prt.getToken(), tokenExpirationMinutes
        );

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("SmashBack – Recuperação de senha");
        msg.setText(body);
        mailSender.send(msg);
    }

    @Transactional
    public void resetarSenha(String token, String novaSenha) {
        Optional<PasswordResetToken> optional = tokenRepo.findByToken(token);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Token inválido");
        }
        PasswordResetToken prt = optional.get();
        if (prt.isUsado() || prt.isExpirado()) {
            throw new IllegalArgumentException("Token expirado ou já usado");
        }
        Usuario user = prt.getUsuario();
        user.setSenha(passwordEncoder.encode(novaSenha));
        prt.setUsado(true);
    }

    @Transactional(readOnly = true)
    public Optional<PasswordResetToken> buscarToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public boolean emailExiste(String email) {
        return usuarioRepo.existsByEmailIgnoreCase(email.trim());
    }
}
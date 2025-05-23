package com.athlos.smashback.repository;

import com.athlos.smashback.model.PasswordResetToken;
import com.athlos.smashback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByToken(String token);
    List<PasswordResetToken> findAllByUsuarioAndUsadoFalse(Usuario usuario);
}

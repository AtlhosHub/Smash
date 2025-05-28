package com.athlos.smashback.controller;

import com.athlos.smashback.dto.ResetPasswordDTO;
import com.athlos.smashback.dto.ResetRequestEmailDTO;
import com.athlos.smashback.model.PasswordResetToken;
import com.athlos.smashback.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {

    private final PasswordResetService resetService;

    public ResetPasswordController(PasswordResetService resetService) {
        this.resetService = resetService;
    }

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(
            @Valid @RequestBody ResetRequestEmailDTO body) {
        resetService.solicitarToken(body.getEmail());
        return ResponseEntity.ok("E-mail enviado.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordDTO body) {
        resetService.resetarSenha(body.getToken(), body.getNovaSenha());
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        Optional<PasswordResetToken> opt = resetService.buscarToken(token);
        if (opt.isEmpty() || opt.get().isUsado() || opt.get().isExpirado()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Token inválido ou expirado");
        }
        return ResponseEntity.ok().build();
    }
}
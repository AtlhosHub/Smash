package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Payload para resetar a senha com token")
public class ResetPasswordDTO {

    @NotBlank(message = "O código de reset é obrigatório")
    @Schema(description = "Código/token enviado por e-mail", example = "3f6a9c2b-...")
    private String token;

    @NotBlank(message = "A nova senha não pode ficar em branco")
    @Size(min = 8, message = "Sua senha deve possuir no mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$",
            message = "Sua senha deve ter ao menos 1 MAIÚSCULO, 1 minúsculo, 1 número e 1 caractere especial"
    )
    @Schema(
            description = "Nova senha do usuário",
            example = "NovaSenha@123",
            minLength = 8
    )
    private String novaSenha;
}
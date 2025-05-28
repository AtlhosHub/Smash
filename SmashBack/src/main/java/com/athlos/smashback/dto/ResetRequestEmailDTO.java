package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Payload para solicitar reset de senha")
public class ResetRequestEmailDTO {

    @NotBlank
    @Email
    @Schema(description = "E-mail vinculado à conta do usuário",
            example = "usuario@exemplo.com")
    private String email;
}

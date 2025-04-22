package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO para autenticação de um usuário")
public class UsuarioLoginDTO {

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "Senha@123")
    private String senha;
}

package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO que representa uma listagem de usuários")
public class UsuarioListaDTO {

    @Schema(description = "Identificador único do usuário", example = "1")
    private int id;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nome;
}

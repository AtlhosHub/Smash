package com.athlos.smashback.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Filtro para buscar usuários com base em critérios específicos")
public class UsuarioFilter {
    @Schema(description = "Nome do usuário para busca", example = "João Silva")
    private String nome;
}


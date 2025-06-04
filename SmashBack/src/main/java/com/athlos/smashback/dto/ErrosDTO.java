package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO que representa um erro de validação")
public class ErrosDTO {
@Schema(description = "Nome da classe onde ocorreu o erro", example = "Aluno")
    private String classe;

    @Schema(description = "Nome do campo onde ocorreu o erro", example = "nome")
    private String campo;

    @Schema(description = "Mensagem de erro", example = "O campo nome não pode ser vazio")
    private String mensagem;
}

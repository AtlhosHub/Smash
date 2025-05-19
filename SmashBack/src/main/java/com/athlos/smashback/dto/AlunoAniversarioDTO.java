package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa o aniversário de um aluno")
public class AlunoAniversarioDTO {
    @Schema(description = "Nome completo do aluno", example = "Carlos Oliveira")
    private String nome;

    @Schema(description = "Data de nascimento do aluno", example = "2005-04-21")
    private LocalDate dataNascimento;
}

package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO que representa informações do comprovante de um aluno")
public class AlunoComprovanteDTO {

    @Schema(description = "Identificador único do aluno", example = "1")
    private int id;

    @Schema(description = "Nome do aluno", example = "Carlos Oliveira")
    private String nome;

    @Schema(description = "Indica se o aluno está ativo", example = "true")
    private Boolean isAtivo;

    @Schema(description = "Data de envio do comprovante", example = "2025-04-21T10:15:30")
    private LocalDateTime dtEnvio;

    @Schema(description = "Status do comprovante", example = "Enviado")
    private String status;
}

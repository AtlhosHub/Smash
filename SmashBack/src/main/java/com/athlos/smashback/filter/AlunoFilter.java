package com.athlos.smashback.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Filtro para buscar alunos com base em critérios específicos")
public class AlunoFilter {

    @Schema(description = "Nome do aluno para busca", example = "Carlos")
    private String nome;

    @Schema(description = "Status do pagamento", example = "Enviado")
    private String status;

    @Schema(description = "Indica se o aluno está ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Data inicial para filtrar envios", example = "2025-01-01")
    private LocalDate dataEnvioFrom;

    @Schema(description = "Data final para filtrar envios", example = "2025-12-31")
    private LocalDate dataEnvioTo;
}

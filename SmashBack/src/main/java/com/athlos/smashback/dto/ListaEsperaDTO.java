package com.athlos.smashback.dto;

import com.athlos.smashback.model.HorarioPref;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO que representa informações de um interessado na lista de espera")
public class ListaEsperaDTO {

    @Schema(description = "Identificador único do interessado", example = "1")
    private int id;

    @Schema(description = "Nome do interessado", example = "Maria Silva")
    private String nome;

    @Schema(
            description = "Data/hora de interesse do interessado; se não for passada, será preenchida com o timestamp atual",
            example = "2025-04-21T14:30:00"
    )
    private LocalDateTime dataInteresse;

    @Schema(description = "Horário de preferência do interessado")
    private HorarioPref horarioPreferencia;
}
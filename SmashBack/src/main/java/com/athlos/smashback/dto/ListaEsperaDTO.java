package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "DTO que representa informações de um interessado na lista de espera")
public class ListaEsperaDTO {

    @Schema(description = "Identificador único do interessado", example = "1")
    private int id;

    @Schema(description = "Nome do interessado", example = "Maria Silva")
    private String nome;

    @Schema(description = "Data de interesse do interessado", example = "2025-04-21")
    private LocalDate dataInteresse;

    @Schema(description = "Horário de preferência do interessado", example = "14:30:00")
    private LocalTime horarioPreferencia;
}

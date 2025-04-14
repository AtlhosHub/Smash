package com.athlos.smashback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ListaEsperaDTO {
    private int id;
    private String nome;
    private LocalDate dataInteresse;
    private Time horarioPreferencia;
}

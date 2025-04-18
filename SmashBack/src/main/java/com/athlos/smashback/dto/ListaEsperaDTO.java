package com.athlos.smashback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ListaEsperaDTO {
    private int id;
    private String nome;
    private LocalDate dataInteresse;
    private LocalTime horarioPreferencia;
}

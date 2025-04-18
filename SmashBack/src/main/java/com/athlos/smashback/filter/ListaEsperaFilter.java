package com.athlos.smashback.filter;

import com.athlos.smashback.model.HorarioPref;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ListaEsperaFilter {
    private String nome;
    private LocalDate dataInteresseFrom;
    private LocalDate dataInteresseTo;
    private HorarioPref horarioPreferencia;
}

package com.athlos.smashback.filter;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AlunoFilter {
    private String nome;
    private String status;
    private Boolean ativo;
    private LocalDate dataEnvioFrom;
    private LocalDate dataEnvioTo;
}

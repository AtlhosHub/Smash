package com.athlos.smashback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AlunoComprovanteDTO {
    private int id;
    private String nome;
    private Boolean isAtivo;
    private LocalDateTime dtEnvio;
    private String status;
}

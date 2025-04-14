package com.athlos.smashback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UsuarioInfoDTO {
    private String nome;
    private String email;
    private String celular;
    private LocalDate dataNascimento;
    private String cargo;
}

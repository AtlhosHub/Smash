package com.athlos.smashback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UsuarioTokenDTO {
    private int id;
    private String nome;
    private String email;
    private String token;
}

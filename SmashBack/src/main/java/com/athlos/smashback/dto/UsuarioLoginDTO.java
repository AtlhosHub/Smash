package com.athlos.smashback.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UsuarioLoginDTO {
    private String email;
    private String senha;
}

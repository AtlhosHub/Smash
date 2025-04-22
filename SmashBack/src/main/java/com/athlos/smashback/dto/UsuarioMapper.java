package com.athlos.smashback.dto;

import com.athlos.smashback.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

public class UsuarioMapper {

    @Schema(description = "Converte um DTO de login de usuário para uma entidade de usuário")
    public static Usuario of(UsuarioLoginDTO usuarioLoginDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioLoginDto.getEmail());
        usuario.setSenha(usuarioLoginDto.getSenha());

        return usuario;
    }

    @Schema(description = "Converte uma entidade de usuário e um token para um DTO de token de usuário")
    public static UsuarioTokenDTO of(Usuario usuario, String token) {
        UsuarioTokenDTO usuarioTokenDto = new UsuarioTokenDTO();

        usuarioTokenDto.setId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }

}
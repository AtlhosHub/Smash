package com.athlos.smashback.dto;

import com.athlos.smashback.model.Usuario;

public class UsuarioMapper {

    public static Usuario of(UsuarioLoginDTO usuarioLoginDto) {
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioLoginDto.getEmail());
        usuario.setSenha(usuarioLoginDto.getSenha());

        return usuario;
    }

    public static UsuarioTokenDTO of(Usuario usuario, String token) {
        UsuarioTokenDTO usuarioTokenDto = new UsuarioTokenDTO();

        usuarioTokenDto.setId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }

}
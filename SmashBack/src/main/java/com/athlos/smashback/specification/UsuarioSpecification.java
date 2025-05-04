package com.athlos.smashback.specification;

import com.athlos.smashback.filter.UsuarioFilter;
import com.athlos.smashback.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {
    public static final String NOME = "nome";

    private UsuarioSpecification(){}

    public static Specification<Usuario> filtrarPor(UsuarioFilter usuarioFilter) {
        return Specification.where(hasNomeLike(usuarioFilter.getNome()));
    }

    private static Specification<Usuario> hasNomeLike(String nome){
        return ((root, query, cb) -> nome == null || nome.isEmpty() ? cb.conjunction() : cb.like(cb.lower(root.get(NOME)), "%"+nome.toLowerCase()+"%"));
    }
}

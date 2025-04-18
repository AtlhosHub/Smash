package com.athlos.smashback.specification;

import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import org.springframework.data.jpa.domain.Specification;

public class AlunoSpecification {
    private static final String NOME = "nome";
    private static final String ATIVO = "ativo";

    private AlunoSpecification() {}

    public static Specification<Aluno> filtrarPor(AlunoFilter alunoFilter){
        return Specification
                .where(hasNomeLike(alunoFilter.getNome()))
                .and(hasPresenteEqual(alunoFilter.getAtivo()));
    }

    private static Specification<Aluno> hasNomeLike(String nome) {
        return ((root, query, cb) -> nome == null || nome.isEmpty() ? cb.conjunction() : cb.like(cb.lower(root.get(NOME)), "%" + nome.toLowerCase() + "%"));
    }

    private static Specification<Aluno> hasPresenteEqual(Boolean ativo) {
        return ((root, query, cb) -> ativo == null ? cb.conjunction() : cb.equal(root.get(ATIVO), ativo));
    }
}

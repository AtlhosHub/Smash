package com.athlos.smashback.specification;

import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.model.ListaEspera;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ListaEsperaSpecification {
    public static final String NOME = "nome";
    public static final String DATA_INTERESSE = "dataInteresse";
    public static final String HORARIO_PREFERENCIA = "horarioPref";

    private ListaEsperaSpecification(){}

    public static Specification<ListaEspera> filtrarPor(ListaEsperaFilter listaEsperaFilter) {
        return Specification
                .where(hasNomeLike(listaEsperaFilter.getNome()))
                .and(hasDataInteresseGreaterThanEqual(listaEsperaFilter.getDataInteresseFrom()))
                .and(hasDataInteresseLessThanEqual(listaEsperaFilter.getDataInteresseTo()))
                .and(hasHorarioPreferenciaEqual(listaEsperaFilter.getHorarioPreferencia()));
    }

    private static Specification<ListaEspera> hasNomeLike(String nome){
        return ((root, query, cb) -> nome == null || nome.isEmpty() ? cb.conjunction() : cb.like(cb.lower(root.get(NOME)), "%"+nome.toLowerCase()+"%"));
    }

    private static Specification<ListaEspera> hasDataInteresseGreaterThanEqual(LocalDate dataInteresseFrom){
        return ((root, query, cb) -> dataInteresseFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(DATA_INTERESSE), dataInteresseFrom));
    }

    private static Specification<ListaEspera> hasDataInteresseLessThanEqual(LocalDate dataInteresseTo){
        return ((root, query, cb) -> dataInteresseTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(DATA_INTERESSE), dataInteresseTo));
    }

    private static Specification<ListaEspera> hasHorarioPreferenciaEqual(HorarioPref horarioPreferencia){
        return ((root, query, cb) -> horarioPreferencia == null ? cb.conjunction() : cb.equal(root.get(HORARIO_PREFERENCIA), horarioPreferencia));
    }
}

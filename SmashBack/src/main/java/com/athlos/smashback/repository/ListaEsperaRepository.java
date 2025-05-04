package com.athlos.smashback.repository;

import com.athlos.smashback.model.ListaEspera;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Integer>, JpaSpecificationExecutor<ListaEspera> {
    boolean existsByNomeAndEmailIgnoreCase(String nome, String email);

    boolean existsByNomeAndEmailIgnoreCaseAndIdIsNot(String nome, String email, int id);

    List<ListaEspera> findAll(@Nullable Specification<ListaEspera> spec);
}

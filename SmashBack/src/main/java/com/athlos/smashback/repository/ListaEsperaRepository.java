package com.athlos.smashback.repository;

import com.athlos.smashback.model.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Integer> {
    boolean existsByNomeAndEmail(String nome, String email);

    boolean existsByNomeAndEmailAndIdIsNot(String nome, String email, int id);
}

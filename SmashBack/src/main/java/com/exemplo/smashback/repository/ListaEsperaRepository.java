package com.exemplo.smashback.repository;

import com.exemplo.smashback.model.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Integer> {
    boolean existsByNomeAndEmail(String nome, String email);

    boolean existsByNomeAndEmailAndIdIsNot(String nome, String email, int id);
}

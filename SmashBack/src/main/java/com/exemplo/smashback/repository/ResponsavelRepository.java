package com.exemplo.smashback.repository;

import com.exemplo.smashback.model.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, Integer> {
    Optional<Responsavel> findByCpf(String cpf);
}

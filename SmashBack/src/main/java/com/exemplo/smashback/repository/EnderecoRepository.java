package com.exemplo.smashback.repository;

import com.exemplo.smashback.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Optional<Endereco> findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCep(String logradouro, String numLogradouro, String bairro, String cidade, String cep);
}

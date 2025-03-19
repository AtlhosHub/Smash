package com.exemplo.smashback.repository;

import com.exemplo.smashback.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    boolean existsById(int id);

    boolean existsByEmailOrCpfOrRg(String email, String cpf, String rg);

    boolean existsByEmailAndIdIsNot(String email, int id);

    boolean existsByCpfAndIdIsNot(String cpf, int id);

    boolean existsByRgAndIdIsNot(String rg, int id);

}

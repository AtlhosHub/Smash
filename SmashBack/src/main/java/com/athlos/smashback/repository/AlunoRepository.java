package com.athlos.smashback.repository;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.model.Aluno;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer>, JpaSpecificationExecutor<Aluno> {

    boolean existsById(int id);

    boolean existsByEmailOrCpfOrRg(String email, String cpf, String rg);

    boolean existsByEmailAndIdIsNot(String email, int id);

    boolean existsByCpfAndIdIsNot(String cpf, int id);

    boolean existsByRgAndIdIsNot(String rg, int id);

    List<Aluno> findAll(@Nullable Specification<Aluno> spec);

}

package com.athlos.smashback.repository;

import com.athlos.smashback.model.Aluno;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer>, JpaSpecificationExecutor<Aluno> {

    boolean existsById(int id);

    boolean existsByEmailAndIdIsNot(String email, int id);

    boolean existsByCpfAndIdIsNot(String cpf, int id);

    boolean existsByRgAndIdIsNot(String rg, int id);

    List<Aluno> findAll(@Nullable Specification<Aluno> spec);

    @Query("""
        SELECT DISTINCT a
        FROM Aluno a
        LEFT JOIN a.responsaveis r
        WHERE LOWER(a.email) = LOWER(:email)
           OR LOWER(r.email) = LOWER(:email)
        """)
    Optional<Aluno> findByEmailOrResponsavelEmail(@Param("email") String email);

    boolean existsByCpfOrRg(String cpf, String rg);

    boolean existsByEmailIgnoreCaseOrCpfOrRg(String email, String cpf, String rg);

    boolean existsByEmailIgnoreCaseAndIdIsNot(String email, int id);

    @Query("SELECT a FROM Aluno a ORDER BY a.dataNascimento")
    List<Aluno> findAniversariantes();

    int countByAtivo(boolean ativo);
}

package com.athlos.smashback.repository;

import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Comprovante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Integer> {

    @Query("SELECT c FROM Comprovante c WHERE c.aluno.id IN :alunos AND YEAR(c.dataEnvio) = :ano AND MONTH(c.dataEnvio) = :mes")
    List<Comprovante> findByAlunoIdInAndMes(@Param("alunos") List<Integer> alunos, @Param("ano") int ano, @Param("mes") int mes);
}

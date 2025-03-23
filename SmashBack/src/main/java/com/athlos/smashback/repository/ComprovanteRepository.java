package com.athlos.smashback.repository;

import com.athlos.smashback.model.Comprovante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Integer> {

    // Busca um comprovante do mês atual para um aluno
    @Query("SELECT c FROM Comprovante c WHERE c.aluno.id = :alunoId AND FUNCTION('YEAR', c.dataEnvio) = :ano AND FUNCTION('MONTH', c.dataEnvio) = :mes")
    List<Comprovante> findByAlunoAndMonth(@Param("alunoId") int alunoId, @Param("ano") int ano, @Param("mes") int mes);
}

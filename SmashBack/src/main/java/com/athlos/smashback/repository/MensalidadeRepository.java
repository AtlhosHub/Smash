package com.athlos.smashback.repository;

import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {


    List<Mensalidade> findByStatusAndDataVencimentoBefore(
            @Param("status") Status status,
            @Param("data") LocalDate data
    );

    @Query("SELECT m FROM Mensalidade m WHERE m.aluno = :aluno AND m.status IN :status ORDER BY m.dataVencimento ASC")
    List<Mensalidade> findByAlunoAndStatusInOrderByDataVencimentoAsc(
            @Param("aluno") Aluno aluno,
            @Param("status") List<Status> status
    );
}
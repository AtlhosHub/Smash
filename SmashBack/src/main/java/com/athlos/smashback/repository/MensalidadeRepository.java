package com.athlos.smashback.repository;

import com.athlos.smashback.dto.GraficoDTO;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("SELECT COUNT(m) FROM Mensalidade m JOIN m.valor v WHERE m.status = 'PAGO' AND YEAR(m.dataPagamento) = YEAR(CURRENT_DATE)  AND v.desconto = true")
    int countMensalidadesDesconto();

    @Query("""
        SELECT new com.athlos.smashback.dto.GraficoDTO(
            MONTH(m.dataVencimento),
            SUM(CASE WHEN m.status = 'ATRASADO' THEN 1 ELSE 0 END),
            SUM(CASE WHEN m.status = 'PAGO' AND v.desconto = false THEN 1 ELSE 0 END),
            SUM(CASE WHEN m.status = 'PAGO' AND v.desconto = true THEN 1 ELSE 0 END)
        )
        FROM Mensalidade m
        JOIN m.valor v
        WHERE m.status IN ('PAGO', 'ATRASADO') AND YEAR(m.dataVencimento) = YEAR(CURRENT_DATE)
        GROUP BY MONTH(m.dataVencimento)
        ORDER BY MONTH(m.dataVencimento)
    """)
    List<GraficoDTO> graficoMensalidade();

    List<Mensalidade> findByAluno(Aluno aluno);

}
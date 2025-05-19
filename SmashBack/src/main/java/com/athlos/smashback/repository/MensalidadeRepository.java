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

    @Query(
        value = "SELECT COUNT(m.id) qtd_descontos FROM Mensalidade m WHERE m.status = 'PAGO' AND MONTH(m.dataPagamento) = MONTH(NOW()) AND YEAR(m.dataPagamento) = YEAR(NOW()) AND DATEDIFF('DAY', m.dataPagamento, m.dataVencimento) >= 12"
        , nativeQuery = true
    )
    int countMensalidadesDesconto();

    @Query(value = """
      SELECT MONTH(m.dataVencimento) AS mes,
      SUM(CASE WHEN m.status = 'ATRASADO' THEN 1 ELSE 0 END) AS atrasados,
      SUM(CASE WHEN m.status = 'PAGO' AND DATEDIFF('DAY',m.dataVencimento, m.dataPagamento) < 12 THEN 1 ELSE 0 END) AS pagos,
      SUM(CASE WHEN m.status = 'PAGO' AND DATEDIFF('DAY', m.dataVencimento, m.dataPagamento) >= 12 THEN 1 ELSE 0 END) AS pagos_com_desconto
      FROM Mensalidade m
      GROUP BY MONTH(m.dataVencimento)
      ORDER BY MONTH(m.dataVencimento);
    """, nativeQuery = true)
    List<GraficoDTO> graficoMensalidade();
}
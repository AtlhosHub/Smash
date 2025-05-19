package com.athlos.smashback.repository;

import com.athlos.smashback.model.ValorMensalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValorMensalidadeRepository extends JpaRepository<ValorMensalidade, Integer> {
//    ValorMensalidade findFirstByOrderByDataInclusaoDesc();

    ValorMensalidade findFirstByManualFalseAndDescontoFalseOrderByDataInclusaoDesc();

    Optional<ValorMensalidade> findByValorAndManual(double valor, boolean manual);

    Optional<ValorMensalidade> findByValorAndDesconto(double valor, boolean desconto);
}

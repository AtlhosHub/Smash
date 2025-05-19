package com.athlos.smashback.dto;

import com.athlos.smashback.model.ValorMensalidade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PagamentoManualDTO {
    @Schema(description = "Valor efetivamente pago", example = "412.54")
    private ValorMensalidade valorPago;

    @Schema(description = "Forma de pagamento (ex: Dinheiro, Cartão)", example = "Dinheiro")
    private String formaPagamento;
}

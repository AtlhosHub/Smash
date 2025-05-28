package com.athlos.smashback.dto;

import com.athlos.smashback.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoManualDTO {

    @Schema(description = "Identificador do aluno", example = "42")
    private Long alunoId;

    @Schema(description = "Novo status da mensalidade", example = "PAGO")
    private Status status;

    @Schema(description = "Data e hora efetiva do pagamento", example = "2025-05-10T14:30:00")
    private LocalDateTime dataPagamento;

    @Schema(description = "Valor efetivamente pago", example = "412.54")
    private Double valorPago;

    @Schema(description = "Forma de pagamento (ex: Dinheiro, Cartão, Pix)", example = "Pix")
    private String formaPagamento;
}
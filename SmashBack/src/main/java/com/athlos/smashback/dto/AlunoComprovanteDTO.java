package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa o status de pagamento de mensalidades de um aluno")
public class AlunoComprovanteDTO {

    @Schema(description = "Identificador único da mensalidade", example = "1")
    private Long idMensalidade;

    @Schema(description = "Identificador único do aluno", example = "1")
    private int id;

    @Schema(description = "Nome completo do aluno", example = "Carlos Oliveira")
    private String nome;

    @Schema(description = "Indica se o aluno está ativo no sistema", example = "true")
    private boolean ativo;

    @Schema(description = "Data e hora em que a mensalidade foi paga (data de envio do comprovante)", example = "2025-04-21T10:15:30")
    private LocalDateTime dataEnvio;

    @Schema(description = "Status atual da mensalidade (PENDENTE, ATRASADO ou PAGO)", example = "PAGO")
    private String status;

    @Schema (description = "Forma de pagamento utilizada", example = "Pix")
    private String formaPagamento;

    @Schema(description = "Valor da mensalidade", example = "150.00")
    private Double valor;
}

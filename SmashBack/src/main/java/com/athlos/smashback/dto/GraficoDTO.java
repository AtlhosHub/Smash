package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO que representa os dados do gráfico da dashboard")
public class GraficoDTO {
    @Schema(description = "Mês de referência das mensalidades (1 = Janeiro, 12 = Dezembro)", example = "4")
    private int mes;

    @Schema(description = "Quantidade de mensalidades atrasadas no mês", example = "3")
    private Long atrasados;

    @Schema(description = "Quantidade de mensalidades pagas no mês sem desconto", example = "10")
    private Long pagos;

    @Schema(description = "Quantidade de mensalidades pagas no mês com desconto", example = "2")
    private Long pagos_com_desconto;

    public GraficoDTO(int mes, Long atrasados, Long pagos, Long pagos_com_desconto) {
        this.mes = mes;
        this.atrasados = atrasados;
        this.pagos = pagos;
        this.pagos_com_desconto = pagos_com_desconto;
    }
}

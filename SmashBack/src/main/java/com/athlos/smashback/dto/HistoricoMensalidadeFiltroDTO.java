package com.athlos.smashback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "Filtro para buscar histórico de mensalidades")
public class HistoricoMensalidadeFiltroDTO {

    @Schema(description = "Data inicial (yyyy-MM-dd) para filtrar vencimento",
            example = "2025-05-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @Schema(description = "Data final (yyyy-MM-dd) para filtrar vencimento",
            example = "2025-05-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateTo;
}
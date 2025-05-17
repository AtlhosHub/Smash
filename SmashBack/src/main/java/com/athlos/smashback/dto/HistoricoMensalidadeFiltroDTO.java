package com.athlos.smashback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "Filtro para buscar histórico de mensalidades")
public class HistoricoMensalidadeFiltroDTO {

    @Schema(description = "Data inicial (dd/MM/yyyy) para filtrar vencimento",
            example = "01/05/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateFrom;

    @Schema(description = "Data final (dd/MM/yyyy) para filtrar vencimento",
            example = "31/05/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateTo;
}
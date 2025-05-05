package com.athlos.smashback.filter;

import com.athlos.smashback.model.HorarioPref;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Filtro para buscar interessados na lista de espera com base em critérios específicos")
public class ListaEsperaFilter {

    @Schema(description = "Nome do interessado para busca", example = "Maria Silva")
    private String nome;

    @Schema(description = "Data inicial de interesse para busca", example = "2025-01-01")
    private LocalDateTime dataInteresseFrom;

    @Schema(description = "Data final de interesse para busca", example = "2025-12-31")
    private LocalDateTime dataInteresseTo;

    @Schema(description = "Horário de preferência do interessado", example = "14:30:00")
    private HorarioPref horarioPreferencia;
}

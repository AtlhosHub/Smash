package com.athlos.smashback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa as configurações de horário de aula")
public class ConfiguracoesHorario {
    @Schema(description = "Identificador único da configuração de horário", example = "1")
    private Integer id;

    @Schema(description = "Horário de início da aula", example = "08:00:00")
    private LocalTime horarioAulaInicio;

    @Schema(description = "Horário de fim da aula", example = "17:00:00")
    private LocalTime horarioAulaFim;
}

package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horario_pref")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Entidade que representa um horário de aula")
public class HorarioPref {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do horário de aula", example = "1")
    private int id;

    @Schema(description = "Horário de aula", example = "14:30:00")
    private LocalTime horarioAula;

    @OneToMany(mappedBy = "horarioPref", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("horarioPref")
    @Schema(description = "Lista de interessados associados a este horário de aula")
    private List<ListaEspera> interessados = new ArrayList<>();
}
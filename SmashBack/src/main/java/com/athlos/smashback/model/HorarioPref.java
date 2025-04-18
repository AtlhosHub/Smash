package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horario_pref")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HorarioPref {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalTime horarioAula;

    @OneToMany(mappedBy = "horarioPref", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("horarioPref")
    private List<ListaEspera> interessados = new ArrayList<>();
}
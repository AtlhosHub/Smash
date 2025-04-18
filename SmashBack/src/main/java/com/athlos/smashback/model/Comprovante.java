package com.athlos.smashback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comprovante")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comprovante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime dataEnvio;
    private int status;
    private String bancoOrigem;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
}

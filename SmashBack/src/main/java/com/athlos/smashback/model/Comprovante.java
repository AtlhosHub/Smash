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
    private String nomeRemetente;
    private Double valor;
    private LocalDateTime dataEnvio;
    private String bancoOrigem;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Override
    public String toString() {
        return "PagamentoExtraido{" +
                "nome_remetente='" + nomeRemetente + '\'' +
                ", valor='" + valor + '\'' +
                ", data_hora='" + dataEnvio + '\'' +
                ", banco_origem='" + bancoOrigem + '\'' +
                '}';
    }
}

package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprovante")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comprovante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nomeRemetente;
    private Double valor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataEnvio;
    private String bancoOrigem;
    private Double valorOriginal;
    private Double valorAplicado;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @OneToMany(mappedBy = "comprovante")
    private List<Mensalidade> mensalidades;

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

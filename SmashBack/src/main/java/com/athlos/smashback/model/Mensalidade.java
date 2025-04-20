package com.athlos.smashback.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.athlos.smashback.model.enums.Status;

@Entity
@Table(name = "mensalidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Mensalidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    private LocalDate dataVencimento;
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = true) // nulo por enquanto para poder testar
    private Double valor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "comprovante_id")
    private Comprovante comprovante;
}
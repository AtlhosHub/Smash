package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.athlos.smashback.model.enums.Status;

@Entity
@Table(name = "mensalidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa uma mensalidade no sistema")
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da mensalidade", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @Schema(description = "Aluno associado à mensalidade")
    @JsonIgnore
    private Aluno aluno;

    @Schema(description = "Data de vencimento da mensalidade", example = "2025-05-01")
    private LocalDate dataVencimento;

    @Schema(description = "Data de pagamento da mensalidade", example = "2025-04-21T10:15:30")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status da mensalidade", example = "PAGO")
    private Status status;

    @Column(nullable = true)
    @Schema(description = "Valor da mensalidade", example = "150.00")
    private Double valor;

    @Column(name = "forma_pagamento", nullable = true)
    @Schema(description = "Forma de pagamento usada", example = "Pix")
    private String formaPagamento;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "comprovante_id")
    @Schema(description = "Comprovante associado à mensalidade")
    @JsonIgnoreProperties("mensalidade")
    private Comprovante comprovante;
}
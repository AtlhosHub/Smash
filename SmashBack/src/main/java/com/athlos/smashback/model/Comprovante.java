package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprovante")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Entidade que representa um comprovante de pagamento")
public class Comprovante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do comprovante", example = "1")
    private int id;

    @Schema(description = "Nome do remetente do pagamento", example = "Carlos Oliveira")
    private String nomeRemetente;

    @Schema(description = "Valor do pagamento", example = "150.00")
    private Double valor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora do envio do comprovante", example = "2025-04-21T10:15:30")
    private LocalDateTime dataEnvio;

    @Schema(description = "Banco de origem do pagamento", example = "Banco do Brasil")
    private String bancoOrigem;

    @Schema(description = "Valor original do pagamento", example = "200.00")
    private Double valorOriginal;

    @Schema(description = "Valor aplicado após descontos ou ajustes", example = "150.00")
    private Double valorAplicado;

    @Schema(description = "Usuário que irá receber o comprovante", example = "Walter White")
    private String usuarioDestino;

    @Schema(description = "Banco do Destinatario", example = "Caixa Econômica Federal")
    private String bancoDestino;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    @Schema(description = "Aluno associado ao comprovante")
    private Aluno aluno;

    @OneToMany(mappedBy = "comprovante")
    @JsonIgnoreProperties({"mensalidades"})
    @Schema(description = "Lista de mensalidades associadas ao comprovante")
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

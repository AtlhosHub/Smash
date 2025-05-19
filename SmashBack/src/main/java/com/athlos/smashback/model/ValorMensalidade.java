package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "valor_mensalidade")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que o valor das mensalidades no sistema")
public class ValorMensalidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do valor de mensalidade", example = "1")
    private int id;

    @NotNull(message = "Valor não pode ser nulo")
    @DecimalMin(value = "0.0", message = "Valor deve ser maior que zero")
    @Schema(description = "Valor da mensalidade", example = "150.00")
    private Double valor;

    @Schema(description = "Valor inserido para pagamentos manuais", example = "false")
    private boolean manual = false;

    @Schema(description = "Valor inserido para pagamentos com desconto", example = "false")
    private boolean desconto = false;

    @CreationTimestamp
    @Schema(description = "Data de inclusão do valor da mensalidade no sistema", example = "2025-04-21T10:15:30")
    private LocalDateTime dataInclusao;

    @OneToMany(mappedBy = "valor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"valor", "aluno", "comprovante"})
    @Schema(description = "Lista de mensalidades associadas a este valor de mensalidade")
    private List<Mensalidade> mensalidades = new ArrayList<>();
}

package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_espera")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa um interessado na lista de espera")
public class ListaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do interessado", example = "1")
    private int id;

    @NotBlank(message = "O nome do interessado não pode ficar em branco")
    @Schema(description = "Nome do interessado", example = "Maria Silva")
    private String nome;

    @Email(message = "O e-mail deve ser válido (Possuir @)")
    @Schema(description = "E-mail do interessado", example = "maria.silva@email.com")
    private String email;

    @PastOrPresent(message = "A data de interesse deve ser uma data presente ou passada")
    @Schema(description = "Data de interesse do interessado", example = "2025-04-21")
    private LocalDateTime dataInteresse;

    @NotBlank(message = "O celular não pode ficar em branco")
    @Schema(description = "Número de celular do interessado", example = "(11) 91234-5678")
    private String celular;

    @Schema(description = "Nome social do interessado, caso aplicável", example = "Joana Silva")
    private String nomeSocial;

    @Schema(description = "Gênero do interessado", example = "Feminino")
    private String genero;

    @Schema(description = "Data de nascimento do interessado", example = "1990-05-15")
    private LocalDate dataNascimento;

    @Schema(description = "Número de telefone do interessado", example = "(11) 1234-5678")
    private String telefone;

    @CreationTimestamp
    @Schema(description = "Data de inclusão do interessado no sistema", example = "2025-04-21T10:15:30")
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    @Schema(description = "Usuário que incluiu o interessado no sistema")
    private Usuario usuarioInclusao;

    @ManyToOne
    @JoinColumn(name = "horario_pref_id")
    @Schema(description = "Horário de aula associado ao interessado")
    private HorarioPref horarioPref;
}

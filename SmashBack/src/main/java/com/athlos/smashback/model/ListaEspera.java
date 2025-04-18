package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_espera")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListaEspera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do interessado não pode ficar em branco")
    private String nome;

    @Email(message = "O e-mail deve ser válido (Possuir @)")
    private String email;

    @PastOrPresent(message = "A data de interesse deve ser uma data presente ou passada")
    private LocalDate dataInteresse;

    @NotBlank(message = "O celular não pode ficar em branco")
    private String celular;

    private String nomeSocial;
    private String genero;
    private LocalDate dataNascimento;
    private String telefone;


    @CreationTimestamp
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    private Usuario usuarioInclusao;

    @ManyToOne
    @JoinColumn(name = "horario_pref_id")
    private HorarioPref horarioPref;
}

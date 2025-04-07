package com.athlos.smashback.model;

import com.athlos.smashback.validation.Cpf;
import com.athlos.smashback.validation.ResponsaveisSeMenor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "aluno")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ResponsaveisSeMenor
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do aluno não pode ficar em branco")
    private String nome;

    @NotBlank(message = "O e-mail não pode ficar em branco")
    @Email(message = "O e-mail deve ser válido (Possuir @)")
    private String email;

    @NotNull(message = "A data de nascimento deve ser preenchida")
    @Past(message = "A data de nascimento deve ser uma data passada")
    private LocalDate dataNascimento;

    @NotBlank(message = "O CPF não pode ficar em branco")
    @Cpf
    private String cpf;

    private String celular;
    private String nacionalidade;
    private String naturalidade;
    private String telefone;
    private String profissao;
    private String rg;
    private boolean ativo;
    private boolean temAtestado;
    private boolean temAssinatura;
    private boolean autorizado;

    @CreationTimestamp
    private LocalDateTime dataInclusao;

    @Valid
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    @JsonIgnoreProperties("alunos")
    @NotNull(message = "Os dados de endereço devem ser preenchidos")
    private Endereco endereco;

    @Valid
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "responsavel_aluno",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    @JsonIgnoreProperties({"alunos"})
    private List<Responsavel> responsaveis;

    @Valid
    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "alunos", "usuariosCadastrados", "interessados"})
    private Usuario usuarioInclusao;

    public boolean isMenor(){
        return Period.between(dataNascimento, LocalDate.now()).getYears() < 18;
    }
}

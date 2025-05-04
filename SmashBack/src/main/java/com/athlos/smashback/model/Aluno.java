package com.athlos.smashback.model;

import com.athlos.smashback.validation.Cpf;
import com.athlos.smashback.validation.ResponsaveisSeMenor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ResponsaveisSeMenor
@Schema(description = "Entidade que representa um aluno no sistema")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do aluno", example = "1")
    private int id;

    @NotBlank(message = "O nome do aluno não pode ficar em branco")
    @Schema(description = "Nome completo do aluno", example = "Carlos Oliveira")
    private String nome;

    @NotBlank(message = "O e-mail não pode ficar em branco")
    @Email(message = "O e-mail deve ser válido (Possuir @)")
    @Schema(description = "E-mail do aluno", example = "carlos.oliveira@email.com")
    private String email;

    @NotNull(message = "A data de nascimento deve ser preenchida")
    @Past(message = "A data de nascimento deve ser uma data passada")
    @Schema(description = "Data de nascimento do aluno", example = "2005-04-21")
    private LocalDate dataNascimento;

    @NotBlank(message = "O CPF não pode ficar em branco")
    @Cpf
    @Schema(description = "CPF do aluno", example = "12345678900")
    private String cpf;

    @NotBlank(message = "O RG não pode ficar em branco")
    @Schema(description = "RG do aluno", example = "123456789")
    private String rg;

    @Schema(description = "Nome social do aluno, caso aplicável", example = "Joana Silva")
    private String nomeSocial;

    @Schema(description = "Gênero do aluno", example = "Masculino")
    private String genero;

    @Schema(description = "Número de celular do aluno", example = "(11) 91234-5678")
    private String celular;

    @Schema(description = "Nacionalidade do aluno", example = "Brasileiro")
    private String nacionalidade;

    @Schema(description = "Naturalidade do aluno", example = "São Paulo")
    private String naturalidade;

    @Schema(description = "Número de telefone do aluno", example = "(11) 1234-5678")
    private String telefone;

    @Schema(description = "Profissão do aluno", example = "Estudante")
    private String profissao;

    @Schema(description = "Indica se o aluno está ativo", example = "true")
    private boolean ativo;

    @Schema(description = "Indica se o aluno possui atestado médico", example = "false")
    private boolean temAtestado;

    @Schema(description = "Descrição de alguma deficiência do aluno, caso aplicável", example = "Nenhuma")
    private String deficiencia;

    @Schema(description = "Indica se o aluno está autorizado para atividades pelo responsável, se aplicável", example = "true")
    private boolean autorizado;

    @CreationTimestamp
    @Schema(description = "Data de inclusão do aluno no sistema", example = "2025-04-21T10:15:30")
    private LocalDateTime dataInclusao;

    @Valid
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    @JsonIgnoreProperties("alunos")
    @NotNull(message = "Os dados de endereço devem ser preenchidos")
    @Schema(description = "Endereço associado ao aluno")
    private Endereco endereco;

    @Valid
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "responsavel_aluno",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    @JsonIgnoreProperties({"alunos"})
    @Schema(description = "Lista de responsáveis associados ao aluno")
    private List<Responsavel> responsaveis;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "alunos", "usuariosCadastrados", "interessados"})
    @Schema(description = "Usuário que incluiu o aluno no sistema")
    private Usuario usuarioInclusao;

    @Schema(description = "Verifica se o aluno é menor de idade", example = "true")
    public boolean isMenor(){
        return Period.between(dataNascimento, LocalDate.now()).getYears() < 18;
    }

    @OneToMany(
            mappedBy = "aluno",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("aluno")
    @Schema(description = "Mensalidades associadas ao aluno")
    private List<Mensalidade> mensalidades = new ArrayList<>();
}
package com.athlos.smashback.model;

import com.athlos.smashback.validation.Cpf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responsavel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa um responsável no sistema")
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do responsável", example = "1")
    private int id;

    @NotBlank(message = "O nome do responsável não pode ficar em branco")
    @Schema(description = "Nome completo do responsável", example = "Ana Pereira")
    private String nome;

    @NotBlank(message = "O CPF do responsável não pode ficar em branco")
    @Cpf(message = "O CPF do responsável deve ser válido")
    @Schema(description = "CPF do responsável", example = "98765432100")
    private String cpf;

    @NotBlank(message = "O celular do responsável não pode ficar em branco")
    @Schema(description = "Número de celular do responsável", example = "(11) 91234-5678")
    private String celular;

    @NotBlank(message = "O e-mail não pode ficar em branco")
    @Email(message = "O e-mail deve ser válido (Possuir @)")
    @Schema(description = "E-mail do responsável", example = "ana.pereira@email.com")
    private String email;

    @Schema(description = "RG do responsável", example = "987654321")
    private String rg;

    @Schema(description = "Número de telefone do responsável", example = "(11) 1234-5678")
    private String telefone;

    @Schema(description = "Nome social do responsável, caso aplicável", example = "Gustavo Pereira")
    private String nomeSocial;

    @Schema(description = "Gênero do responsável", example = "Feminino")
    private String genero;

    @ManyToMany(mappedBy = "responsaveis")
    @JsonIgnoreProperties("responsaveis")
    @Schema(description = "Lista de alunos associados ao responsável")
    private List<Aluno> alunos = new ArrayList<>();
}

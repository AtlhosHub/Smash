package com.athlos.smashback.model;

import com.athlos.smashback.validation.Cpf;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responsavel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Responsavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do responsável não pode ficar em branco")
    private String nome;

    @NotBlank(message = "O CPF do responsável não pode ficar em branco")
    @Cpf(message = "O CPF do responsável deve ser válido")
    private String cpf;

    @NotBlank(message = "O celular do responsável não pode ficar em branco")
    private String celular;

    private String rg;
    private String telefone;

    @ManyToMany(mappedBy = "responsaveis")
    @JsonIgnoreProperties("responsaveis")
    private List<Aluno> alunos = new ArrayList<>();
}

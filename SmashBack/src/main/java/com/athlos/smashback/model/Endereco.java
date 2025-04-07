package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "endereco")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O logradouro não pode ficar em branco")
    private String logradouro;

    @NotBlank(message = "O número não pode ficar em branco")
    private String numLogradouro;

    @NotBlank(message = "O bairro não pode ficar em branco")
    private String bairro;

    @NotBlank(message = "O cidade não pode ficar em branco")
    private String cidade;

    @NotBlank(message = "O cep não pode ficar em branco")
    private String cep;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("endereco")
    private List<Aluno> alunos = new ArrayList<>();
}

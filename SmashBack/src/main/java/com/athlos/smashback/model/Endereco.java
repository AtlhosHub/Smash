package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "endereco")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa um endereço no sistema")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do endereço", example = "1")
    private int id;

    @NotBlank(message = "O logradouro não pode ficar em branco")
    @Schema(description = "Logradouro do endereço", example = "Rua das Flores")
    private String logradouro;

    @NotBlank(message = "O número não pode ficar em branco")
    @Schema(description = "Número do logradouro", example = "123")
    private String numLogradouro;

    @NotBlank(message = "O bairro não pode ficar em branco")
    @Schema(description = "Bairro do endereço", example = "Centro")
    private String bairro;

    @NotBlank(message = "A cidade não pode ficar em branco")
    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "O cep não pode ficar em branco")
    @Schema(description = "CEP do endereço", example = "12345-678")
    private String cep;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("endereco")
    @Schema(description = "Lista de alunos associados ao endereço")
    private List<Aluno> alunos = new ArrayList<>();
}

package com.athlos.smashback.dto;

import com.athlos.smashback.model.Endereco;
import com.athlos.smashback.model.Responsavel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Getter @Setter @AllArgsConstructor  @NoArgsConstructor
@Schema(description = "DTO que representa informações detalhadas de um aluno")
public class InfoAlunoDTO {
    @Schema(description = "Nome completo do aluno", example = "Carlos Oliveira")
    private String nome;

    @Schema(description = "E-mail do aluno", example = "carlos.oliveira@email.com")
    private String email;

    @Schema(description = "Data de nascimento do aluno", example = "2005-04-21")
    private LocalDate dataNascimento;

    @Schema(description = "CPF do aluno", example = "12345678900")
    private String cpf;

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

    @Schema(description = "Data de inclusão do aluno no sistema", example = "2025-04-21T10:15:30")
    private LocalDateTime dataInclusao;

    @JsonIgnoreProperties("alunos")
    @Schema(description = "Endereço associado ao aluno")
    private Endereco endereco;

    @JsonIgnoreProperties({"alunos"})
    @Schema(description = "Lista de responsáveis associados ao aluno")
    private List<Responsavel> responsaveis;

    @Schema(description = "Usuário que incluiu o aluno no sistema")
    private int usuarioInclusao;

    @Schema(description = "Verifica se o aluno é menor de idade", example = "true")
    public boolean isMenor(){
        return Period.between(dataNascimento, LocalDate.now()).getYears() < 18;
    }
}

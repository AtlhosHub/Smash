package com.exemplo.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String logradouro;
    private String numLogradouro;
    private String bairro;
    private String cidade;
    private String cep;

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("endereco")
    private List<Aluno> alunos = new ArrayList<>();

    public Endereco(){}

    public Endereco(int id, String logradouro, String numLogradouro, String bairro, String cidade, String cep, List<Aluno> alunos) {
        this.id = id;
        this.logradouro = logradouro;
        this.numLogradouro = numLogradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
        this.alunos = alunos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumLogradouro() {
        return numLogradouro;
    }

    public void setNumLogradouro(String numLogradouro) {
        this.numLogradouro = numLogradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}

package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String email;
    private String nacionalidade;
    private String naturalidade;
    private LocalDate dataNascimento;
    private String telefone;
    private String celular;
    private String profissao;
    private String rg;
    private String cpf;
    private boolean ativo;
    private boolean temAtestado;
    private boolean temAssinatura;
    private boolean autorizado;

    @CreationTimestamp
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    @JsonIgnoreProperties("alunos")
    private Endereco endereco;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "responsavel_aluno",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    @JsonIgnoreProperties({"alunos"})
    private List<Responsavel> responsaveis;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "alunos", "usuariosCadastrados", "interessados"})
    private Usuario usuarioInclusao;

    public Aluno() {}

    public Aluno(int id, String nome, String email, String nacionalidade, String naturalidade, LocalDate dataNascimento, String telefone, String celular, String profissao, String rg, String cpf, boolean ativo, boolean temAtestado, boolean temAssinatura, boolean autorizado, LocalDateTime dataInclusao, Endereco endereco, List<Responsavel> responsaveis, Usuario usuarioInclusao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.nacionalidade = nacionalidade;
        this.naturalidade = naturalidade;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.celular = celular;
        this.profissao = profissao;
        this.rg = rg;
        this.cpf = cpf;
        this.ativo = ativo;
        this.temAtestado = temAtestado;
        this.temAssinatura = temAssinatura;
        this.autorizado = autorizado;
        this.dataInclusao = dataInclusao;
        this.endereco = endereco;
        this.responsaveis = responsaveis;
        this.usuarioInclusao = usuarioInclusao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isTemAtestado() {
        return temAtestado;
    }

    public void setTemAtestado(boolean temAtestado) {
        this.temAtestado = temAtestado;
    }

    public boolean isTemAssinatura() {
        return temAssinatura;
    }

    public void setTemAssinatura(boolean temAssinatura) {
        this.temAssinatura = temAssinatura;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public boolean isMenor() {
        return Period.between(dataNascimento, LocalDate.now()).getYears() < 18;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }
}

package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String celular;
    private LocalDate dataNascimento;
    private String cargo;
    private boolean deletado = false;

    @CreationTimestamp
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    private Usuario usuarioInclusao;

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    private List<Usuario> usuariosCadastrados = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuarioInclusao")
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuarioInclusao")
    private List<ListaEspera> interessados = new ArrayList<>();

    public Usuario(){}

    public Usuario(int id, String nome, String email, String senha, String celular, LocalDate dataNascimento, String cargo, boolean deletado, LocalDateTime dataInclusao, Usuario usuarioInclusao, List<Usuario> usuariosCadastrados, List<Aluno> alunos, List<ListaEspera> interessados) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.dataNascimento = dataNascimento;
        this.cargo = cargo;
        this.deletado = deletado;
        this.dataInclusao = dataInclusao;
        this.usuarioInclusao = usuarioInclusao;
        this.usuariosCadastrados = usuariosCadastrados;
        this.alunos = alunos;
        this.interessados = interessados;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public List<Usuario> getUsuariosCadastrados() {
        return usuariosCadastrados;
    }

    public void setUsuariosCadastrados(List<Usuario> usuariosCadastrados) {
        this.usuariosCadastrados = usuariosCadastrados;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<ListaEspera> getInteressados() {
        return interessados;
    }

    public void setInteressados(List<ListaEspera> interessados) {
        this.interessados = interessados;
    }
}

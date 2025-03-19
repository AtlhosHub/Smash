package com.exemplo.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_espera")
public class ListaEspera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String email;
    private LocalDate dataInteresse;
    private Time horarioPreferencia;
    private String celular;

    @CreationTimestamp
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    private Usuario usuarioInclusao;

    public ListaEspera(){}

    public ListaEspera(int id, String nome, String email, LocalDate dataInteresse, Time horarioPreferencia, String celular, LocalDateTime dataInclusao, Usuario usuarioInclusao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataInteresse = dataInteresse;
        this.horarioPreferencia = horarioPreferencia;
        this.celular = celular;
        this.dataInclusao = dataInclusao;
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

    public LocalDate getDataInteresse() {
        return dataInteresse;
    }

    public void setDataInteresse(LocalDate dataInteresse) {
        this.dataInteresse = dataInteresse;
    }

    public Time getHorarioPreferencia() {
        return horarioPreferencia;
    }

    public void setHorarioPreferencia(Time horarioPreferencia) {
        this.horarioPreferencia = horarioPreferencia;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
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
}

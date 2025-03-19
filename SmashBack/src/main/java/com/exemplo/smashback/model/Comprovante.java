package com.exemplo.smashback.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comprovante")
public class Comprovante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime dataEnvio;
    private int status;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    public Comprovante() {}

    public Comprovante(int id, LocalDateTime dataEnvio, int status, Aluno aluno) {
        this.id = id;
        this.dataEnvio = dataEnvio;
        this.status = status;
        this.aluno = aluno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}

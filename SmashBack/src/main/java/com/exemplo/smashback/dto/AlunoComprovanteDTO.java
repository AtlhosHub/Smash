package com.exemplo.smashback.dto;

import java.time.LocalDateTime;

public class AlunoComprovanteDTO {
    private int id;
    private String nome;
    private Boolean isAtivo;
    private LocalDateTime dtEnvio;
    private String status;

    public AlunoComprovanteDTO(){}

    public AlunoComprovanteDTO(int id, String nome, Boolean isAtivo, LocalDateTime dtEnvio, String status) {
        this.id = id;
        this.nome = nome;
        this.isAtivo = isAtivo;
        this.dtEnvio = dtEnvio;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public LocalDateTime getDtEnvio() {
        return dtEnvio;
    }

    public void setDtEnvio(LocalDateTime dtEnvio) {
        this.dtEnvio = dtEnvio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

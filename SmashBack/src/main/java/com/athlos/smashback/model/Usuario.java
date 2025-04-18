package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do usuário não pode ficar em branco")
    private String nome;

    @Email(message = "O e-mail deve ser válido (Possuir @)")
    private String email;

    @Size(min = 8, message = "Sua senha deve possuir no mínimo 8 caracteres, 1 caractere MAIÚSCULO, 1 caractere minúsculo, 1 número e 1 caractere especial (!@#$%&*,.;)")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")
    private String senha;

    @NotBlank(message = "O celular não pode ficar em branco")
    private String celular;

    @NotNull(message = "A data de nascimento deve ser preenchida")
    @Past(message = "A data deve ser uma data passada")
    private LocalDate dataNascimento;

    private String nomeSocial;
    private String genero;
    private String telefone;
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
}

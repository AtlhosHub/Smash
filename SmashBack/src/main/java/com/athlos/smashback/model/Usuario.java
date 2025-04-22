package com.athlos.smashback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Entidade que representa um usuário no sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do usuário", example = "1")
    private int id;

    @NotBlank(message = "O nome do usuário não pode ficar em branco")
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @Email(message = "O e-mail deve ser válido (Possuir @)")
    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Size(min = 8, message = "Sua senha deve possuir no mínimo 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$", message = "Sua senha deve possuir no mínimo 8 caracteres, 1 caractere MAIÚSCULO, 1 caractere minúsculo, 1 número e 1 caractere especial (!@#$%&*,.;)")
    @Schema(description = "Senha do usuário", example = "Senha@123")
    private String senha;

    @NotBlank(message = "O celular não pode ficar em branco")
    @Schema(description = "Número de celular do usuário", example = "(11) 91234-5678")
    private String celular;

    @NotNull(message = "A data de nascimento deve ser preenchida")
    @Past(message = "A data deve ser uma data passada")
    @Schema(description = "Data de nascimento do usuário", example = "1990-05-15")
    private LocalDate dataNascimento;

    @Schema(description = "Nome social do usuário, caso aplicável", example = "Joana Silva")
    private String nomeSocial;

    @Schema(description = "Gênero do usuário", example = "Masculino")
    private String genero;

    @Schema(description = "Número de telefone do usuário", example = "(11) 1234-5678")
    private String telefone;

    @Schema(description = "Cargo do usuário", example = "Administrador")
    private String cargo;

    @Schema(description = "Indica se o usuário foi deletado", example = "false")
    private boolean deletado = false;

    @CreationTimestamp
    @Schema(description = "Data de inclusão do usuário no sistema", example = "2025-04-21T10:15:30")
    private LocalDateTime dataInclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_inclusao_id")
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    @Schema(description = "Usuário que incluiu este usuário no sistema")
    private Usuario usuarioInclusao;

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"usuarioInclusao", "usuariosCadastrados", "alunos", "interessados"})
    @Schema(description = "Lista de usuários cadastrados por este usuário")
    private List<Usuario> usuariosCadastrados = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuarioInclusao")
    @Schema(description = "Lista de alunos associados a este usuário")
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInclusao", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuarioInclusao")
    @Schema(description = "Lista de interessados associados a este usuário")
    private List<ListaEspera> interessados = new ArrayList<>();
}

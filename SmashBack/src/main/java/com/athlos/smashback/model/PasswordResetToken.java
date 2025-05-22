    package com.athlos.smashback.model;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "password_reset_token")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class PasswordResetToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false, unique = true)
        private String token;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @Column(nullable = false)
        private boolean usado = false;

        @Column(nullable = false)
        private LocalDateTime expiracao;

        public boolean isExpirado() {
            return expiracao.isBefore(LocalDateTime.now());
        }
    }
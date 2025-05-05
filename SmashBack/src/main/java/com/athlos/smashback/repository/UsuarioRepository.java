package com.athlos.smashback.repository;

import com.athlos.smashback.model.Usuario;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdIsNot(String email, int id);

    List<Usuario> findAllByDeletado(boolean deletado);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    List<Usuario> findAll(@Nullable Specification<Usuario> spec);
}

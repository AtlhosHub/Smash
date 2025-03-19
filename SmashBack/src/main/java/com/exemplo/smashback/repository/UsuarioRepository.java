package com.exemplo.smashback.repository;

import com.exemplo.smashback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, int id);

    List<Usuario> findAllByDeletado(boolean deletado);
}

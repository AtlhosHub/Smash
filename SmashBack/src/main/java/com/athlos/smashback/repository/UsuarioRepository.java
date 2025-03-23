package com.athlos.smashback.repository;

import com.athlos.smashback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, int id);

    List<Usuario> findAllByDeletado(boolean deletado);
}

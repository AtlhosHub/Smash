package com.athlos.smashback.repository;

import com.athlos.smashback.model.HorarioPref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface HorarioPrefRepository extends JpaRepository<HorarioPref, Integer> {
    boolean existsByHorarioAula(LocalTime horarioAula);

    boolean existsByHorarioAulaAndIdIsNot(LocalTime horarioAula, int id);
}

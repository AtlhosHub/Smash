package com.athlos.smashback.repository;

import com.athlos.smashback.model.HorarioPref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface HorarioPrefRepository extends JpaRepository<HorarioPref, Integer> {
    boolean existsByHorarioAulaInicioAndHorarioAulaFim(LocalTime horarioAulaInicio, LocalTime horarioAulaFim);

    boolean existsByHorarioAulaInicioAndHorarioAulaFimAndIdIsNot(LocalTime horarioAulaInicio, LocalTime horarioAulaFim, int id);}

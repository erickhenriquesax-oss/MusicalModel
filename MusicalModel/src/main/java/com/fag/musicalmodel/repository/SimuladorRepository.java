package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.SimuladorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SimuladorRepository extends JpaRepository<SimuladorModel, Long> {

    Optional<SimuladorModel> findByUsuarioModel_IdUsuario(Long idUsuario);
}

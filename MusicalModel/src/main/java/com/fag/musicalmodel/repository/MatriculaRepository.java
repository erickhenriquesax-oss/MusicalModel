package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {

    Optional<MatriculaModel> findByUsuarioModel_IdUsuario(Long idUsuario);
    Optional<MatriculaModel> findByCursoModel_IdCurso(Long idCurso);
}

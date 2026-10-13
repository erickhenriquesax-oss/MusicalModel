package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {
    List<MatriculaModel> findByUsuarioModel_IdUsuario(Long idUsuario);
    List<MatriculaModel> findByCursoModel_IdCurso(Long idCurso);
}
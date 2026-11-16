package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {
    List<MatriculaModel> findByUsuarioModel_IdUsuario(Long idUsuario);
    List<MatriculaModel> findByCursoModel_IdCurso(Long idCurso);

    boolean existsByUsuarioModel_IdUsuarioAndCursoModel_IdCursoAndStatus(Long idUsuario, Long idCurso, StatusMatricula status);

    boolean existsByUsuarioModel_IdUsuarioAndCursoModel_IdCursoAndStatusAndIdMatriculaNot(Long idUsuario, Long idCurso, StatusMatricula status, Long idMatricula);
}

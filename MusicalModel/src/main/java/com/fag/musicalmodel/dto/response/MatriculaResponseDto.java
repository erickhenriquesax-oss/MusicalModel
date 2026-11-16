package com.fag.musicalmodel.dto.response;

import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.MatriculaModel;

import java.io.Serializable;

public record MatriculaResponseDto(
        Long idMatricula,
        StatusMatricula status,
        Long idUsuario,
        String nomeUsuario,
        Long idCurso,
        String tituloCurso
) implements Serializable {
    public MatriculaResponseDto(MatriculaModel matricula) {
        this(
                matricula.getIdMatricula(),
                matricula.getStatus(),
                matricula.getUsuarioModel().getIdUsuario(),
                matricula.getUsuarioModel().getNome(),
                matricula.getCursoModel().getIdCurso(),
                matricula.getCursoModel().getTitulo()
        );
    }
}

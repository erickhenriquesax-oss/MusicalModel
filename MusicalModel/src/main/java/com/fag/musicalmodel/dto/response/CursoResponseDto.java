package com.fag.musicalmodel.dto.response;

import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.InstrumentoModel;

import java.io.Serializable;

public record CursoResponseDto(
        Long idCurso,
        String titulo,
        String descricao,
        int duracao,
        String conteudo,
        boolean ativo,
        Long idInstrumento,
        String nomeInstrumento
) implements Serializable {
    public CursoResponseDto(CursoModel curso) {
        this(
                curso.getIdCurso(),
                curso.getTitulo(),
                curso.getDescricao(),
                curso.getDuracao(),
                curso.getConteudo(),
                curso.isAtivo(),
                idInstrumento(curso.getInstrumentoModel()),
                curso.getInstrumentoModel() != null ? curso.getInstrumentoModel().getNome() : null
        );
    }

    private static Long idInstrumento(InstrumentoModel instrumento) {
        return instrumento != null ? instrumento.getIdInstrumento() : null;
    }
}

package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotNull;

public record MatriculaDto(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long idUsuario,

        @NotNull(message = "O ID do curso é obrigatório")
        Long idCurso
) {
}

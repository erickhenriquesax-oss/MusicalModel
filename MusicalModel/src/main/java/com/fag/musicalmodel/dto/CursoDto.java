package com.fag.musicalmodel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CursoDto(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O instrumento é obrigatório")
        Long idInstrumento,

        @Positive(message = "A duração deve ser positiva")
        int duracao,

        @NotBlank(message = "O conteúdo é obrigatório")
        String conteudo
) {
}

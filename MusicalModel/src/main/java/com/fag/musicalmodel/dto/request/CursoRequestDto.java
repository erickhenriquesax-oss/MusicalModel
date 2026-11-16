package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CursoRequestDto(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 70, message = "O título deve ter no máximo 70 caracteres")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        String descricao,

        @NotNull(message = "O instrumento é obrigatório")
        Long idInstrumento,

        @NotNull(message = "A duração é obrigatória")
        @Positive(message = "A duração deve ser positiva")
        @Max(value = 999, message = "A duração deve ser no máximo 999")
        Integer duracao,

        @NotBlank(message = "O conteúdo é obrigatório")
        @Size(max = 255, message = "O conteúdo deve ter no máximo 255 caracteres")
        String conteudo
) {
}

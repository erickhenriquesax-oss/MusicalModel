package com.fag.musicalmodel.dto;

import jakarta.validation.constraints.NotBlank;

public record InstrumentoDto(
        @NotBlank(message = "O nome do instrumento é obrigatório")
        String nome,

        String descricao
) {
}

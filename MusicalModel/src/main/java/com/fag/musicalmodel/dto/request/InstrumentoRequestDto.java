package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InstrumentoRequestDto(
        @NotBlank(message = "O nome do instrumento é obrigatório")
        String nome,

        String descricao
) {
}

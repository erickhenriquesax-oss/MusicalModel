package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CanalNomeRequestDto(
        @NotBlank(message = "O nome do canal é obrigatório")
        @Size(max = 30, message = "O nome do canal deve ter no máximo 30 caracteres")
        String nome
) {
}

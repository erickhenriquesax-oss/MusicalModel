package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Novo nome (descrição) de um canal da mesa de som, ex.: "Voz principal" ou "Violão". */
public record CanalNomeRequestDto(
        @NotBlank(message = "O nome do canal é obrigatório")
        @Size(max = 30, message = "O nome do canal deve ter no máximo 30 caracteres")
        String nome
) {
}

package com.fag.musicalmodel.dto;

import jakarta.validation.constraints.NotNull;

public record CanalDto(
        @NotNull(message = "O ID do simulador é obrigatório")
        Long idSimulador,

        int agudo,
        int medio,
        int grave,
        int volume
) {
}

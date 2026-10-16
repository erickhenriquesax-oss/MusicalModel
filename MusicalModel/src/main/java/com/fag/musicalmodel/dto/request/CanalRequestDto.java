package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotNull;

public record CanalRequestDto(
        @NotNull(message = "O ID do simulador é obrigatório")
        Long idSimulador,

        int agudo,
        int medio,
        int grave,
        int volume
) {
}

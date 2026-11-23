package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CanalRequestDto(
        @NotNull(message = "O agudo é obrigatório")
        @Min(value = 0, message = "O agudo deve estar entre 0 e 100")
        @Max(value = 100, message = "O agudo deve estar entre 0 e 100")
        Integer agudo,

        @NotNull(message = "O médio é obrigatório")
        @Min(value = 0, message = "O médio deve estar entre 0 e 100")
        @Max(value = 100, message = "O médio deve estar entre 0 e 100")
        Integer medio,

        @NotNull(message = "O grave é obrigatório")
        @Min(value = 0, message = "O grave deve estar entre 0 e 100")
        @Max(value = 100, message = "O grave deve estar entre 0 e 100")
        Integer grave,

        @NotNull(message = "O volume é obrigatório")
        @Min(value = 0, message = "O volume deve estar entre 0 e 100")
        @Max(value = 100, message = "O volume deve estar entre 0 e 100")
        Integer volume
) {
}

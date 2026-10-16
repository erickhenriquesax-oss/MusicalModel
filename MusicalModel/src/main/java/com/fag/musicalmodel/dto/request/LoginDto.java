package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty(message = "Email é obrigatório preencher")
        String email,

        @NotEmpty(message = "Senha é obrigatório preencher")
        String senha
) {
}

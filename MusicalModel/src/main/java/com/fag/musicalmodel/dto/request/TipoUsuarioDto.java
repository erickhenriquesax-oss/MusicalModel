package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TipoUsuarioDto(
        @NotBlank(message = "A descrição do tipo de usuário é obrigatória")
        String descricao
) {
}

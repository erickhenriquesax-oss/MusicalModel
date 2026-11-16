package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoUsuarioRequestDto(
        @NotBlank(message = "A descrição do tipo de usuário é obrigatória")
        @Size(max = 50, message = "A descrição deve ter no máximo 50 caracteres")
        String descricao
) {
}

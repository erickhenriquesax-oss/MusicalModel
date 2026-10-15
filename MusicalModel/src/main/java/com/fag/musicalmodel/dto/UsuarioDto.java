package com.fag.musicalmodel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDto(
        @NotNull(message = "O tipo de usuário é obrigatório")
        Long idTipoUsuario,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
        String senha,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        String cep,
        String endereco,
        String telefone,
        Long idInstrumento,
        String complemento,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        Long numero) {
}

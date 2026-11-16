package com.fag.musicalmodel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
        @NotNull(message = "O tipo de usuário é obrigatório")
        Long idTipoUsuario,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 72, message = "A senha deve conter entre 6 e 72 caracteres")
        String senha,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres")
        String cpf,

        @Size(max = 11, message = "O CEP deve ter no máximo 11 caracteres")
        String cep,

        @Size(max = 100, message = "O endereço deve ter no máximo 100 caracteres")
        String endereco,

        @Size(max = 13, message = "O telefone deve ter no máximo 13 caracteres")
        String telefone,

        Long idInstrumento,

        @Size(max = 50, message = "O complemento deve ter no máximo 50 caracteres")
        String complemento,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 70, message = "O nome deve ter no máximo 70 caracteres")
        String nome,

        Long numero
) {
}

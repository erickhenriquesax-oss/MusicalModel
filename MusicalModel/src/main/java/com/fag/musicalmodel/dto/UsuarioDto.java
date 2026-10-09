package com.fag.musicalmodel.dto;

import com.fag.musicalmodel.model.InstrumentoModel;

public record UsuarioDto(
        Long idTipoUsuario,
        String senha,
        String email,
        String cpf,
        String cep,
        String endereco,
        Long telefone,
        Long idInstrumento,
        String complemento,
        String nome,
        Long numero) {
}

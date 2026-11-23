package com.fag.musicalmodel.dto.response;

import com.fag.musicalmodel.model.UsuarioModel;

public record LoginResponse(
        String token,
        Long idUsuario,
        String nome,
        String role,
        Long idInstrumento,
        String nomeInstrumento
) {
    public LoginResponse(String token, UsuarioModel usuario) {
        this(
                token,
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getUserRole() != null ? usuario.getUserRole().name() : null,
                usuario.getInstrumentoModel() != null ? usuario.getInstrumentoModel().getIdInstrumento() : null,
                usuario.getInstrumentoModel() != null ? usuario.getInstrumentoModel().getNome() : null
        );
    }
}

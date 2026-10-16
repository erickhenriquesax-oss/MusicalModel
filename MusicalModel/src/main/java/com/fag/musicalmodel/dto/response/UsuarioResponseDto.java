package com.fag.musicalmodel.dto.response;

import com.fag.musicalmodel.model.UsuarioModel;

public record UsuarioResponseDto(
        Long idUsuario,
        String nome,
        String email,
        String cpf,
        boolean ativo,
        boolean assinatura,
        int horasEstudadas,
        String cep,
        String endereco,
        Long numero,
        String complemento,
        String telefone,
        Long idTipoUsuario,
        String tipoUsuarioDescricao,
        Long idInstrumento,
        String instrumentoNome
) {
    public UsuarioResponseDto(UsuarioModel usuario) {
        this(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.isAtivo(),
                usuario.isAssinatura(),
                usuario.getHoras_estudadas(),
                usuario.getCep(),
                usuario.getEndereco(),
                usuario.getNumero(),
                usuario.getComplemento(),
                usuario.getTelefone(),
                usuario.getTipo_usuario() != null ? usuario.getTipo_usuario().getIdTipoUsuario() : null,
                usuario.getTipo_usuario() != null ? usuario.getTipo_usuario().getDescricao() : null,
                usuario.getInstrumentoModel() != null ? usuario.getInstrumentoModel().getIdInstrumento() : null,
                usuario.getInstrumentoModel() != null ? usuario.getInstrumentoModel().getNome() : null
        );
    }
}

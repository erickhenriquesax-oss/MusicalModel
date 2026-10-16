package com.fag.musicalmodel.enums;

public enum UserRole {
    ALUNO(1L, "ROLE_ALUNO"),
    ADMIN(2L, "ROLE_ADMIN");

    private final Long codigo;          // id da tabela tb_tipo_usuario
    private final String authority;    // usado pelo Spring Security

    UserRole(Long codigo, String authority) {
        this.codigo = codigo;
        this.authority = authority;
    }

    public Long getCodigo() { return codigo; }
    public String getAuthority() { return authority; }


    public static UserRole fromCodigo(Long codigo) {
        for (UserRole r : values()) {
            if (r.codigo.equals(codigo)) return r;
        }
        throw new IllegalArgumentException("Código de role desconhecido: " + codigo);
    }

    public static UserRole fromDescricao(String descricao) {
        if (descricao == null) return null;
        switch (descricao.trim().toUpperCase()) {
            case "ALUNO":   return ALUNO;
            case "ADMIN":   return ADMIN;
            default: throw new IllegalArgumentException("Descrição de role desconhecida: " + descricao);
        }
    }
}
package com.fag.musicalmodel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_instrumento")
@Setter @Getter
public class InstrumentoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInstrumento;

    @Column(length = 50, nullable = false, unique = true)
    private String nome;

    @Column(length = 255, unique = true)
    private String descricao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "instrumentoModel",fetch = FetchType.LAZY)
    private Set<UsuarioModel> usuarioModel = new HashSet<UsuarioModel>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "instrumentoModel", fetch = FetchType.LAZY)
    private Set<CursoModel> cursoModel = new HashSet<CursoModel>();
}

package com.fag.musicalmodel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tb_curso")
@Getter @Setter
public class CursoModel implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(length = 70, nullable = false,  unique = true)
    private String titulo;

    @Column(length = 100, nullable = false)
    private String descricao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modalidade")
    private ModalidadeModel modalidadeModel;

    @Column(length = 3, nullable = false)
    private int duracao;

    @Column( nullable = false)
    private String conteudo;

    @Column
    private boolean ativo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "cursoModel", fetch = FetchType.LAZY)
    private Set<MatriculaModel> matriculaModel = new HashSet<>();





}

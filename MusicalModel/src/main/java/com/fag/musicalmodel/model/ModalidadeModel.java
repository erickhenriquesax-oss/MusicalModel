package com.fag.musicalmodel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_modalidade")
@Getter @Setter
public class ModalidadeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModalidade;

    @Column(length = 50, nullable = false,  unique = true)
    private String descricao;

    //quando enviarmos uma modalidade que solicita via api, vai mapear/esperar uma coleção de cursos, como pode não ter, deixamos apenas para escrita, não sendo necessário ler e tals
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "modalidadeModel", fetch = FetchType.LAZY) //LAZY carregamento lento, não precisa fazer a consulta toda hora, EAGER iria buscar a editora e também todas sub consultas pra trazer os livro que fazem parte da editora
    private Set<CursoModel> cursoModel = new HashSet<>();
}

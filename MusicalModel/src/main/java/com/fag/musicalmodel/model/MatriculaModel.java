package com.fag.musicalmodel.model;

import com.fag.musicalmodel.enums.StatusMatricula;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tb_matricula")
@Getter @Setter
public class MatriculaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMatricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuarioModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private CursoModel cursoModel;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusMatricula status;




}

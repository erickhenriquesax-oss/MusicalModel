package com.fag.musicalmodel.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tb_canal")

@Getter @Setter
public class CanalModel implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCanal;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "id_simulador",  nullable = false)
    private SimuladorModel simuladorModel;

    @Column(name = "agudo")
    private int agudo;

    @Column(name = "medio")
    private int medio;

    @Column(name = "grave")
    private int grave;

    @Column
    private int volume;
}
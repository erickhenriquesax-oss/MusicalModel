package com.fag.musicalmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_simulador")
@Getter @Setter
public class SimuladorModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSimulador;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private UsuarioModel usuarioModel;

    @Column(name = "volume_master")
    private Integer volume_master;

    @OneToMany(mappedBy = "simuladorModel",  fetch = FetchType.LAZY)
    private Set<CanalModel> canalModel = new HashSet<CanalModel>();



}

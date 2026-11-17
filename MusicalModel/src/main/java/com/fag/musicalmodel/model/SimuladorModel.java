package com.fag.musicalmodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_simulador")
@Getter @Setter
public class SimuladorModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSimulador;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private UsuarioModel usuarioModel;

    @Column(name = "volume_master")
    private Integer volume_master;

    // Os canais são criados pelo CanalService e removidos junto com o simulador.
    @OneToMany(mappedBy = "simuladorModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idCanal ASC")
    private List<CanalModel> canalModel = new ArrayList<>();
}

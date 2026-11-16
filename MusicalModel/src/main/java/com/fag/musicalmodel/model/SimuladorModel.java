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

    public static final int QUANTIDADE_CANAIS = 8;
    public static final int VALOR_PADRAO_CANAL = 50;

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

    // Os canais pertencem ao simulador: são criados e removidos junto com ele.
    @OneToMany(mappedBy = "simuladorModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idCanal ASC")
    private List<CanalModel> canalModel = new ArrayList<>();

    /** Cria os 8 canais da mesa de som com agudo, médio, grave e volume em 50. */
    public void criarCanaisPadrao() {
        for (int i = 0; i < QUANTIDADE_CANAIS; i++) {
            CanalModel canal = new CanalModel();
            canal.setAgudo(VALOR_PADRAO_CANAL);
            canal.setMedio(VALOR_PADRAO_CANAL);
            canal.setGrave(VALOR_PADRAO_CANAL);
            canal.setVolume(VALOR_PADRAO_CANAL);
            canal.setSimuladorModel(this);
            this.canalModel.add(canal);
        }
    }
}

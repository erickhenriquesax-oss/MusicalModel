package com.fag.musicalmodel.service;

import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.SimuladorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimuladorService {

    public static final int VOLUME_MASTER_PADRAO = 80;

    private final SimuladorRepository simuladorRepository;
    private final CanalService canalService;

    public SimuladorService(SimuladorRepository simuladorRepository, CanalService canalService) {
        this.simuladorRepository = simuladorRepository;
        this.canalService = canalService;
    }

    /**
     * Cria o simulador do usuário recém-cadastrado (volume master 80) e pede ao CanalService os 8 canais.
     * Roda na mesma transação do cadastro: se algo falhar, nada é gravado.
     */
    @Transactional
    public SimuladorModel criarSimulador(UsuarioModel usuario) {
        SimuladorModel simulador = new SimuladorModel();
        simulador.setVolume_master(VOLUME_MASTER_PADRAO);
        simulador.setUsuarioModel(usuario);
        simulador = simuladorRepository.save(simulador);

        List<CanalModel> canais = canalService.criarCanaisPadrao(simulador);
        simulador.getCanalModel().addAll(canais);
        return simulador;
    }

    @Transactional
    public SimuladorModel getSimuladorByUsuarioModel_IdUsuario(Long idUsuario) {
        return simuladorRepository.findByUsuarioModel_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Simulador não encontrado para o usuário: " + idUsuario));
    }

    @Transactional
    public void deleteSimulador(Long id){
        if (!simuladorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Simulador não encontrado: " + id);
        }
        simuladorRepository.deleteById(id);
    }

    @Transactional
    public SimuladorModel updateVolumeMaster(Long idSimulador, Integer volumeMaster) {
        SimuladorModel simulador = simuladorRepository.findById(idSimulador)
                .orElseThrow(() -> new ResourceNotFoundException("Simulador não encontrado: " + idSimulador));
        simulador.setVolume_master(volumeMaster);
        return simuladorRepository.save(simulador);
    }
}

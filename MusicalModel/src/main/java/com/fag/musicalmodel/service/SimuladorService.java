package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.SimuladorRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.SimuladorRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SimuladorService {

    private final SimuladorRepository simuladorRepository;
    private final UsuarioRepository usuarioRepository;

    public SimuladorService(SimuladorRepository simuladorRepository, UsuarioRepository usuarioRepository) {
        this.simuladorRepository = simuladorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SimuladorModel getSimuladorByUsuarioModel_IdUsuario(Long idUsuario) {
        return simuladorRepository.findByUsuarioModel_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Simulador não encontrado para o usuário: " + idUsuario));
    }

    @Transactional
    public SimuladorModel saveSimulador(SimuladorRequestDto simuladorDto){
        SimuladorModel simulador = new SimuladorModel();
        simulador.setVolume_master(simuladorDto.volume_master());

        if (simuladorDto.id_usuario() != null) {
            UsuarioModel usuario = usuarioRepository.findById(simuladorDto.id_usuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + simuladorDto.id_usuario()));
            simulador.setUsuarioModel(usuario);
        }

        return simuladorRepository.save(simulador);
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

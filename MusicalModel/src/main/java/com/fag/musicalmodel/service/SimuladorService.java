package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.SimuladorDto;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.SimuladorRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return simuladorRepository.findByUsuarioModel_IdUsuario(idUsuario).orElse(null);
    }

    @Transactional
    public SimuladorModel saveSimulador(SimuladorDto simuladorDto){
        try {
            SimuladorModel simulador = new SimuladorModel();
            simulador.setVolume_master(simuladorDto.volume_master());

            if (simuladorDto.id_usuario() != null) {
                UsuarioModel usuario = usuarioRepository.findById(simuladorDto.id_usuario()).orElse(null);
                simulador.setUsuarioModel(usuario);
            }

            return simuladorRepository.save(simulador);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void deleteSimulador(Long id){
        simuladorRepository.deleteById(id);
    }
}

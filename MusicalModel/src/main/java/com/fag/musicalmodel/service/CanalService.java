package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.CanalDto;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.repository.CanalRepository;
import com.fag.musicalmodel.repository.SimuladorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanalService {

    private final SimuladorRepository simuladorRepository;
    private final CanalRepository canalRepository;

    public CanalService(CanalRepository canalRepository, SimuladorRepository simuladorRepository) {
        this.canalRepository = canalRepository;
        this.simuladorRepository = simuladorRepository;
    }

    @Transactional
    public CanalModel saveCanal(CanalDto canalDto) {
        CanalModel canalModel = new CanalModel();
        canalModel.setAgudo(50);
        canalModel.setMedio(50);
        canalModel.setGrave(50);
        canalModel.setVolume(50);

        if (canalDto.idSimulador() != null) {
            SimuladorModel simulador = simuladorRepository.findById(canalDto.idSimulador())
                    .orElseThrow(() -> new RuntimeException("Simulador não encontrado: " + canalDto.idSimulador()));
            canalModel.setSimuladorModel(simulador);
        }

        return canalRepository.save(canalModel);
    }

    @Transactional
    public CanalModel updateCanal(Long id, CanalDto canalDto) {
        CanalModel canalModel = canalRepository.findCanalModelByIdCanal(id);
        canalModel.setGrave(canalDto.grave());
        canalModel.setMedio(canalDto.medio());
        canalModel.setAgudo(canalDto.agudo());
        canalModel.setVolume(canalDto.volume());
        return canalRepository.save(canalModel);
    }

    @Transactional
    public List<CanalModel> findCanalByIdSimulador(Long id) {
        return canalRepository.findBySimuladorModel_IdSimulador(id);
    }
}

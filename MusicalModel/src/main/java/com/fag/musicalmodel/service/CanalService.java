package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.CanalRequestDto;
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
    public CanalModel saveCanal(CanalRequestDto canalRequestDto) {
        CanalModel canalModel = new CanalModel();
        canalModel.setAgudo(50);
        canalModel.setMedio(50);
        canalModel.setGrave(50);
        canalModel.setVolume(50);

        if (canalRequestDto.idSimulador() != null) {
            SimuladorModel simulador = simuladorRepository.findById(canalRequestDto.idSimulador())
                    .orElseThrow(() -> new RuntimeException("Simulador não encontrado: " + canalRequestDto.idSimulador()));
            canalModel.setSimuladorModel(simulador);
        }

        return canalRepository.save(canalModel);
    }

    @Transactional
    public CanalModel updateCanal(Long id, CanalRequestDto canalRequestDto) {
        CanalModel canalModel = canalRepository.findCanalModelByIdCanal(id);
        canalModel.setGrave(canalRequestDto.grave());
        canalModel.setMedio(canalRequestDto.medio());
        canalModel.setAgudo(canalRequestDto.agudo());
        canalModel.setVolume(canalRequestDto.volume());
        return canalRepository.save(canalModel);
    }

    @Transactional
    public List<CanalModel> findCanalByIdSimulador(Long id) {
        return canalRepository.findBySimuladorModel_IdSimulador(id);
    }
}

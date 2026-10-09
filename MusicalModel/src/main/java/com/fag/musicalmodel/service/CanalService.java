package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.CanalDto;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.repository.CanalRepository;
import com.fag.musicalmodel.repository.SimuladorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanalService {

    private SimuladorRepository simuladorRepository;
    private CanalRepository canalRepository;

    public CanalService(CanalRepository canalRepository) {
        this.canalRepository = canalRepository;
    }

    @Transactional
    public CanalModel saveCanal(CanalDto canalDto) {
        CanalModel canalModel = new CanalModel();
        canalModel.setAgudo(50);
        canalModel.setMedio(50);
        canalModel.setGrave(50);
        canalModel.setVolume(50);

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

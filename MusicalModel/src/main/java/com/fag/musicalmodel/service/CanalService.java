package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.CanalRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.repository.CanalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Os canais são criados automaticamente com o simulador (8 canais em 50) no cadastro do usuário;
 * aqui só há leitura e ajuste dos valores.
 */
@Service
public class CanalService {

    private final CanalRepository canalRepository;

    public CanalService(CanalRepository canalRepository) {
        this.canalRepository = canalRepository;
    }

    @Transactional
    public CanalModel updateCanal(Long id, CanalRequestDto canalRequestDto) {
        CanalModel canalModel = canalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal não encontrado: " + id));
        canalModel.setGrave(canalRequestDto.grave());
        canalModel.setMedio(canalRequestDto.medio());
        canalModel.setAgudo(canalRequestDto.agudo());
        canalModel.setVolume(canalRequestDto.volume());
        return canalRepository.save(canalModel);
    }

    @Transactional
    public List<CanalModel> findCanalByIdSimulador(Long id) {
        return canalRepository.findBySimuladorModel_IdSimuladorOrderByIdCanalAsc(id);
    }
}

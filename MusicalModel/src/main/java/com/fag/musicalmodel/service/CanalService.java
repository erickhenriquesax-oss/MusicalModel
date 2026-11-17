package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.CanalNomeRequestDto;
import com.fag.musicalmodel.dto.request.CanalRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.repository.CanalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Canais da mesa de som. São criados aqui quando o simulador nasce (8 canais em 50)
 * e depois só têm os valores e o nome ajustados; tudo fica gravado no banco entre as sessões.
 */
@Service
public class CanalService {

    public static final int QUANTIDADE_CANAIS = 8;
    public static final int VALOR_PADRAO_CANAL = 50;

    private final CanalRepository canalRepository;

    public CanalService(CanalRepository canalRepository) {
        this.canalRepository = canalRepository;
    }

    /** Cria os 8 canais do simulador: "Canal 1" a "Canal 8", com agudo, médio, grave e volume em 50. */
    @Transactional
    public List<CanalModel> criarCanaisPadrao(SimuladorModel simulador) {
        List<CanalModel> canais = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE_CANAIS; i++) {
            CanalModel canal = new CanalModel();
            canal.setNome("Canal " + i);
            canal.setAgudo(VALOR_PADRAO_CANAL);
            canal.setMedio(VALOR_PADRAO_CANAL);
            canal.setGrave(VALOR_PADRAO_CANAL);
            canal.setVolume(VALOR_PADRAO_CANAL);
            canal.setSimuladorModel(simulador);
            canais.add(canal);
        }
        return canalRepository.saveAll(canais);
    }

    @Transactional
    public CanalModel updateCanal(Long id, CanalRequestDto canalRequestDto) {
        CanalModel canalModel = buscarCanal(id);
        canalModel.setGrave(canalRequestDto.grave());
        canalModel.setMedio(canalRequestDto.medio());
        canalModel.setAgudo(canalRequestDto.agudo());
        canalModel.setVolume(canalRequestDto.volume());
        return canalRepository.save(canalModel);
    }

    @Transactional
    public CanalModel renomearCanal(Long id, CanalNomeRequestDto canalNomeRequestDto) {
        CanalModel canalModel = buscarCanal(id);
        canalModel.setNome(canalNomeRequestDto.nome().trim());
        return canalRepository.save(canalModel);
    }

    @Transactional
    public List<CanalModel> findCanalByIdSimulador(Long id) {
        return canalRepository.findBySimuladorModel_IdSimuladorOrderByIdCanalAsc(id);
    }

    private CanalModel buscarCanal(Long id) {
        return canalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal não encontrado: " + id));
    }
}

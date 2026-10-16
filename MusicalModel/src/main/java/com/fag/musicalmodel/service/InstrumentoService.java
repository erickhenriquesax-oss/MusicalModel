package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.InstrumentoRequestDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentoService {

    private final InstrumentoRepository instrumentoRepository;

    public InstrumentoService(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    @Cacheable(value = "instrumentos")
    @Transactional
    public List<InstrumentoModel> getAllInstrumentos(){
        return instrumentoRepository.findAll();
    }

    @CacheEvict(value = "instrumentos", allEntries = true)
    @Transactional
    public InstrumentoModel saveInstrumento(InstrumentoRequestDto instrumentoRequestDto){
        try {
            InstrumentoModel instrumento = new InstrumentoModel();
            instrumento.setNome(instrumentoRequestDto.nome());
            instrumento.setDescricao(instrumentoRequestDto.descricao());

            return instrumentoRepository.save(instrumento);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(value = "instrumentos", allEntries = true)
    @Transactional
    public void deleteInstrumento(Long id){
        instrumentoRepository.deleteById(id);
    }
}

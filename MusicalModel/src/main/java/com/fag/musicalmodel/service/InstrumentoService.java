package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.InstrumentoRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
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
        InstrumentoModel instrumento = new InstrumentoModel();
        instrumento.setNome(instrumentoRequestDto.nome());
        instrumento.setDescricao(instrumentoRequestDto.descricao());

        return instrumentoRepository.save(instrumento);
    }

    @CacheEvict(value = "instrumentos", allEntries = true)
    @Transactional
    public void deleteInstrumento(Long id){
        if (!instrumentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instrumento não encontrado: " + id);
        }
        instrumentoRepository.deleteById(id);
        instrumentoRepository.flush(); // aplica já, para que um instrumento em uso vire 409 tratado
    }
}

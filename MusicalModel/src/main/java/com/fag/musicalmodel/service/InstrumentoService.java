package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.InstrumentoDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentoService {

    private final InstrumentoRepository instrumentoRepository;

    public InstrumentoService(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    @Transactional
    public List<InstrumentoModel> getAllInstrumentos(){
        return instrumentoRepository.findAll();
    }

    @Transactional
    public InstrumentoModel saveInstrumento(InstrumentoDto instrumentoDto){
        try {
            InstrumentoModel instrumento = new InstrumentoModel();
            instrumento.setNome(instrumentoDto.nome());
            instrumento.setDescricao(instrumentoDto.descricao());

            return instrumentoRepository.save(instrumento);
        } catch (Exception e){
            new Exception("Erro");
            System.out.println("Erro ao salvar instrumento");
            return null;
        }
    }

    @Transactional
    public void deleteInstrumento(Long id){
        instrumentoRepository.deleteById(id);
    }
}

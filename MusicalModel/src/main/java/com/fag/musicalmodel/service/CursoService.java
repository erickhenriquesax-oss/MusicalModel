package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.CursoRequestDto;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.repository.CursoRepository;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrumentoRepository instrumentoRepository;

    public CursoService(CursoRepository cursoRepository, InstrumentoRepository instrumentoRepository) {
        this.cursoRepository = cursoRepository;
        this.instrumentoRepository = instrumentoRepository;
    }

    @Cacheable(value = "cursos")
    @Transactional
    public List<CursoModel> getAllCursos(){
        return cursoRepository.findAll();
    }

    @CacheEvict(value = "cursos", allEntries = true)
    @Transactional
    public CursoModel saveCurso(CursoRequestDto cursoRequestDto){
        try {
            CursoModel curso = new CursoModel();
            curso.setTitulo(cursoRequestDto.titulo());
            curso.setDescricao(cursoRequestDto.descricao());
            curso.setDuracao(cursoRequestDto.duracao());
            curso.setConteudo(cursoRequestDto.conteudo());
            curso.setAtivo(true);

            if(cursoRequestDto.idInstrumento() != null){
                InstrumentoModel instrumento = instrumentoRepository.findById(cursoRequestDto.idInstrumento()).get();
                curso.setInstrumentoModel(instrumento);
            }

            return cursoRepository.save(curso);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(value = "cursos", allEntries = true)
    @Transactional
    public CursoModel updateCurso(Long id, CursoRequestDto cursoRequestDto){
        try{
            CursoModel cursoUpdate = cursoRepository.findById(id).get();
            cursoUpdate.setTitulo(cursoRequestDto.titulo());
            cursoUpdate.setDescricao(cursoRequestDto.descricao());
            cursoUpdate.setDuracao(cursoRequestDto.duracao());
            cursoUpdate.setConteudo(cursoRequestDto.conteudo());
            cursoUpdate.setInstrumentoModel(instrumentoRepository.findById(cursoRequestDto.idInstrumento()).get());
            return cursoRepository.save(cursoUpdate);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(value = "cursos", allEntries = true)
    @Transactional
    public void deleteCurso(Long id){
        cursoRepository.deleteById(id);
    }
}

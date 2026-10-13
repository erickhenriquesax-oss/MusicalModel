package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.CursoDto;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.repository.CursoRepository;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public List<CursoModel> getAllCursos(){
        return cursoRepository.findAll();
    }

    @Transactional
    public CursoModel saveCurso(CursoDto cursoDto){
        try {
            CursoModel curso = new CursoModel();
            curso.setTitulo(cursoDto.titulo());
            curso.setDescricao(cursoDto.descricao());
            curso.setDuracao(cursoDto.duracao());
            curso.setConteudo(cursoDto.conteudo());
            curso.setAtivo(true);

            if(cursoDto.idInstrumento() != null){
                InstrumentoModel instrumento = instrumentoRepository.findById(cursoDto.idInstrumento()).get();
                curso.setInstrumentoModel(instrumento);
            }

            return cursoRepository.save(curso);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public CursoModel updateCurso(Long id,CursoDto cursoDto){
        try{
            CursoModel cursoUpdate = cursoRepository.findById(id).get();
            cursoUpdate.setTitulo(cursoDto.titulo());
            cursoUpdate.setDescricao(cursoDto.descricao());
            cursoUpdate.setDuracao(cursoDto.duracao());
            cursoUpdate.setConteudo(cursoDto.conteudo());
            cursoUpdate.setInstrumentoModel(instrumentoRepository.findById(cursoDto.idInstrumento()).get());
            return cursoRepository.save(cursoUpdate);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void deleteCurso(Long id){
        cursoRepository.deleteById(id);
    }
}

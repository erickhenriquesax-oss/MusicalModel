package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.CursoRequestDto;
import com.fag.musicalmodel.dto.response.CursoResponseDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.repository.CursoRepository;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    public List<CursoResponseDto> getAllCursos(){
        return cursoRepository.findAll().stream().map(CursoResponseDto::new).toList();
    }

    @CacheEvict(value = "cursos", allEntries = true)
    @Transactional
    public CursoResponseDto saveCurso(CursoRequestDto cursoRequestDto){
        CursoModel curso = new CursoModel();
        curso.setTitulo(cursoRequestDto.titulo());
        curso.setDescricao(cursoRequestDto.descricao());
        curso.setDuracao(cursoRequestDto.duracao());
        curso.setConteudo(cursoRequestDto.conteudo());
        curso.setAtivo(true);
        curso.setInstrumentoModel(buscarInstrumento(cursoRequestDto.idInstrumento()));

        return new CursoResponseDto(cursoRepository.save(curso));
    }

    @Caching(evict = {
            @CacheEvict(value = "cursos", allEntries = true),
            @CacheEvict(value = "matriculas", allEntries = true)
    })
    @Transactional
    public CursoResponseDto updateCurso(Long id, CursoRequestDto cursoRequestDto){
        CursoModel cursoUpdate = buscarCurso(id);
        cursoUpdate.setTitulo(cursoRequestDto.titulo());
        cursoUpdate.setDescricao(cursoRequestDto.descricao());
        cursoUpdate.setDuracao(cursoRequestDto.duracao());
        cursoUpdate.setConteudo(cursoRequestDto.conteudo());
        cursoUpdate.setInstrumentoModel(buscarInstrumento(cursoRequestDto.idInstrumento()));
        return new CursoResponseDto(cursoRepository.saveAndFlush(cursoUpdate));
    }

    @CacheEvict(value = "cursos", allEntries = true)
    @Transactional
    public CursoResponseDto updateAtivo(Long id, boolean ativo){
        CursoModel curso = buscarCurso(id);
        curso.setAtivo(ativo);
        return new CursoResponseDto(cursoRepository.save(curso));
    }

    @Caching(evict = {
            @CacheEvict(value = "cursos", allEntries = true),
            @CacheEvict(value = "matriculas", allEntries = true)
    })
    @Transactional
    public void deleteCurso(Long id){
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso não encontrado: " + id);
        }
        cursoRepository.deleteById(id);
        cursoRepository.flush();
    }

    private CursoModel buscarCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + id));
    }

    private InstrumentoModel buscarInstrumento(Long idInstrumento) {
        return instrumentoRepository.findById(idInstrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Instrumento não encontrado: " + idInstrumento));
    }
}

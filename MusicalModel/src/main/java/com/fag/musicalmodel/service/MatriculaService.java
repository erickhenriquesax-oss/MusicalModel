package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.MatriculaRequestDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.MatriculaModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.CursoRepository;
import com.fag.musicalmodel.repository.MatriculaRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Cacheable(value = "matriculas")
    @Transactional
    public List<MatriculaModel> getAllMatriculas(){
        return matriculaRepository.findAll();
    }


    @Transactional
    public List<MatriculaModel> getMatriculaByIdCurso(Long idCurso){
        return matriculaRepository.findByCursoModel_IdCurso(idCurso).stream().toList();
    }

    @Transactional
    public List<MatriculaModel> getMatriculaByIdUsuario(Long idUsuario){
        return matriculaRepository.findByUsuarioModel_IdUsuario(idUsuario).stream().toList();
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public MatriculaModel saveMatricula(MatriculaRequestDto matriculaRequestDto){
        try {
            MatriculaModel matricula = new MatriculaModel();

            UsuarioModel usuario = usuarioRepository.findById(matriculaRequestDto.idUsuario()).get();
            matricula.setUsuarioModel(usuario);

            CursoModel curso = cursoRepository.findById(matriculaRequestDto.idCurso()).get();
            matricula.setCursoModel(curso);

            matricula.setStatus(StatusMatricula.ATIVA);

            return matriculaRepository.save(matricula);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public MatriculaModel atualizarStatus(Long id, StatusMatricula novoStatus){
        MatriculaModel matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada: " + id));

        matricula.setStatus(novoStatus);
        return matriculaRepository.save(matricula);
    }

    @Transactional
    public List<MatriculaModel> getMatriculaByUsuarioModel_IdUsuario(Long idUsuario){
        return matriculaRepository.findByUsuarioModel_IdUsuario(idUsuario);
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public void deleteMatricula(Long id){
        matriculaRepository.deleteById(id);
    }
}

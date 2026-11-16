package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.MatriculaRequestDto;
import com.fag.musicalmodel.dto.response.MatriculaResponseDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.exception.BusinessException;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.model.MatriculaModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.CursoRepository;
import com.fag.musicalmodel.repository.MatriculaRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
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

    // O cache guarda DTOs (e não entidades) para não armazenar proxies lazy do Hibernate,
    // que não podem ser inicializados depois de lidos do Redis.
    @Cacheable(value = "matriculas")
    @Transactional
    public List<MatriculaResponseDto> getAllMatriculas(){
        return toDto(matriculaRepository.findAll());
    }


    @Transactional
    public List<MatriculaResponseDto> getMatriculaByIdCurso(Long idCurso){
        return toDto(matriculaRepository.findByCursoModel_IdCurso(idCurso));
    }

    @Transactional
    public List<MatriculaResponseDto> getMatriculaByIdUsuario(Long idUsuario){
        return toDto(matriculaRepository.findByUsuarioModel_IdUsuario(idUsuario));
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public MatriculaResponseDto saveMatricula(MatriculaRequestDto matriculaRequestDto){
        UsuarioModel usuario = usuarioRepository.findById(matriculaRequestDto.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + matriculaRequestDto.idUsuario()));
        CursoModel curso = cursoRepository.findById(matriculaRequestDto.idCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + matriculaRequestDto.idCurso()));

        if (!usuario.isAtivo()) {
            throw BusinessException.conflito("Usuário desativado não pode ser matriculado");
        }
        if (!curso.isAtivo()) {
            throw BusinessException.conflito("Não é possível se matricular em um curso inativo");
        }
        if (matriculaRepository.existsByUsuarioModel_IdUsuarioAndCursoModel_IdCursoAndStatus(
                usuario.getIdUsuario(), curso.getIdCurso(), StatusMatricula.ATIVA)) {
            throw BusinessException.conflito("O usuário já possui uma matrícula ativa neste curso");
        }

        MatriculaModel matricula = new MatriculaModel();
        matricula.setUsuarioModel(usuario);
        matricula.setCursoModel(curso);
        matricula.setStatus(StatusMatricula.ATIVA);

        return new MatriculaResponseDto(matriculaRepository.save(matricula));
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public MatriculaResponseDto atualizarStatus(Long id, StatusMatricula novoStatus){
        MatriculaModel matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada: " + id));

        // Reativar não pode gerar duas matrículas ativas para o mesmo usuário e curso
        if (novoStatus == StatusMatricula.ATIVA && matriculaRepository
                .existsByUsuarioModel_IdUsuarioAndCursoModel_IdCursoAndStatusAndIdMatriculaNot(
                        matricula.getUsuarioModel().getIdUsuario(), matricula.getCursoModel().getIdCurso(),
                        StatusMatricula.ATIVA, id)) {
            throw BusinessException.conflito("O usuário já possui outra matrícula ativa neste curso");
        }

        matricula.setStatus(novoStatus);
        return new MatriculaResponseDto(matriculaRepository.save(matricula));
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public void deleteMatricula(Long id){
        if (!matriculaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Matrícula não encontrada: " + id);
        }
        matriculaRepository.deleteById(id);
    }

    private List<MatriculaResponseDto> toDto(List<MatriculaModel> matriculas) {
        return matriculas.stream().map(MatriculaResponseDto::new).toList();
    }
}

package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.TipoUsuarioRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.repository.TipoUsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @CacheEvict(value = "tipousuario", allEntries = true)
    @Transactional
    public TipoUsuarioModel saveTipoUsuario(TipoUsuarioRequestDto tipoUsuarioRequestDto){
        TipoUsuarioModel tipoUsuarioModel = new TipoUsuarioModel();
        tipoUsuarioModel.setDescricao(tipoUsuarioRequestDto.descricao());

        return tipoUsuarioRepository.save(tipoUsuarioModel);
    }

    @Cacheable(value = "tipousuario")
    @Transactional
    public List<TipoUsuarioModel> getAllTipoUsuario(){
        return tipoUsuarioRepository.findAll();
    }

    @CacheEvict(value = "tipousuario", allEntries = true)
    @Transactional
    public void deleteTipoUsuario(Long id){
        if (!tipoUsuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de usuário não encontrado: " + id);
        }
        tipoUsuarioRepository.deleteById(id);
        tipoUsuarioRepository.flush(); // aplica já, para que um tipo em uso vire 409 tratado
    }
}

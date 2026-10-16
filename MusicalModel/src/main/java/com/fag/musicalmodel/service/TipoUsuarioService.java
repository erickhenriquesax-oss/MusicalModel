package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.request.TipoUsuarioRequestDto;
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
        try{
            TipoUsuarioModel tipoUsuarioModel = new TipoUsuarioModel();
            tipoUsuarioModel.setDescricao(tipoUsuarioRequestDto.descricao());

            return tipoUsuarioRepository.save(tipoUsuarioModel);
        }catch(Exception e){
            new Exception(e.getMessage());
            return null;
        }
    }

    @Cacheable(value = "tipousuario")
    @Transactional
    public List<TipoUsuarioModel> getAllTipoUsuario(){
        return tipoUsuarioRepository.findAll();
    }

    @CacheEvict(value = "tipousuario", allEntries = true)
    @Transactional
    public void deleteTipoUsuario(Long id){
        tipoUsuarioRepository.deleteById(id);
    }
}

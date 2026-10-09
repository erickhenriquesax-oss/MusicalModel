package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.TipoUsuarioDto;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.repository.TipoUsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;


    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Transactional
    public TipoUsuarioModel saveTipoUsuario(TipoUsuarioDto tipoUsuarioDto){
        try{
            TipoUsuarioModel tipoUsuarioModel = new TipoUsuarioModel();
            tipoUsuarioModel.setDescricao(tipoUsuarioDto.descricao());

            return tipoUsuarioRepository.save(tipoUsuarioModel);
        }catch(Exception e){
            new Exception(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<TipoUsuarioModel> getAllTipoUsuario(){
        return tipoUsuarioRepository.findAll();
    }

    @Transactional
    public void deleteTipoUsuario(Long id){
        tipoUsuarioRepository.deleteById(id);
    }
}

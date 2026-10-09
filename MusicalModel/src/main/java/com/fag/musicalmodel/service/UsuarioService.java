package com.fag.musicalmodel.service;


import com.fag.musicalmodel.dto.UsuarioDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import com.fag.musicalmodel.repository.SimuladorRepository;
import com.fag.musicalmodel.repository.TipoUsuarioRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SimuladorRepository simuladorRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, InstrumentoRepository instrumentoRepository, TipoUsuarioRepository tipoUsuarioRepository, SimuladorRepository simuladorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.instrumentoRepository = instrumentoRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.simuladorRepository = simuladorRepository;
    }

    @Transactional
    public List<UsuarioModel> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public UsuarioModel findById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    @Transactional
    public UsuarioModel saveUsuario(UsuarioDto usuarioDto) {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setNome(usuarioDto.nome());
        usuarioModel.setEmail(usuarioDto.email());
        usuarioModel.setSenha(usuarioDto.senha());
        usuarioModel.setCpf(usuarioDto.cpf());
        usuarioModel.setAtivo(true);
        usuarioModel.setCep(usuarioDto.cep());
        usuarioModel.setEndereco(usuarioDto.endereco());
        usuarioModel.setNumero(usuarioDto.numero());
        usuarioModel.setTelefone(usuarioDto.telefone());
        usuarioModel.setAssinatura(false);
        usuarioModel.setComplemento(usuarioDto.complemento());
        usuarioModel.setHoras_estudadas(0);

        SimuladorModel simuladorModel = new SimuladorModel();
        simuladorModel.setUsuarioModel(usuarioModel);
        usuarioModel.setSimuladorModel(simuladorModel);
        if (usuarioDto.idInstrumento() != null) {
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioDto.idInstrumento()).get();
            usuarioModel.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioDto.idTipoUsuario()).get();
        usuarioModel.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioModel);
    }


    @Transactional
    public UsuarioModel updateUsuario(Long id, UsuarioDto usuarioDto) {

        UsuarioModel usuarioUpdate = usuarioRepository.findById(id).get();
        usuarioUpdate.setNome(usuarioDto.nome());
        usuarioUpdate.setEmail(usuarioDto.email());
        usuarioUpdate.setSenha(usuarioDto.senha());
        usuarioUpdate.setCpf(usuarioDto.cpf());
        usuarioUpdate.setCep(usuarioDto.cep());
        usuarioUpdate.setEndereco(usuarioDto.endereco());
        usuarioUpdate.setNumero(usuarioDto.numero());
        usuarioUpdate.setTelefone(usuarioDto.telefone());
        usuarioUpdate.setComplemento(usuarioDto.complemento());

        if (usuarioDto.idInstrumento() != null){
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioDto.idInstrumento()).get();
            usuarioUpdate.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioDto.idTipoUsuario()).get();
        usuarioUpdate.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioUpdate);
    }

    @Transactional
    public UsuarioModel updateAtivo(Long id, Boolean ativo) {
        UsuarioModel usuarioUpdate = usuarioRepository.findById(id).get();
        usuarioUpdate.setAtivo(ativo);
        return usuarioRepository.save(usuarioUpdate);
    }

    @Transactional UsuarioModel updateAssinatura(Long id, Boolean assinatura){
        UsuarioModel usuarioUpdate = usuarioRepository.findById(id).get();
        usuarioUpdate.setAssinatura(assinatura);

        return usuarioRepository.save(usuarioUpdate);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}



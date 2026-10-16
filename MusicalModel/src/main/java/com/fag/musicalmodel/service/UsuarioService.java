package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.UsuarioSaveDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import com.fag.musicalmodel.repository.SimuladorRepository;
import com.fag.musicalmodel.repository.TipoUsuarioRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SimuladorRepository simuladorRepository;
    @Autowired
    private PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          InstrumentoRepository instrumentoRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          SimuladorRepository simuladorRepository) {
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
    public UsuarioModel saveUsuario(UsuarioSaveDto usuarioSaveDto) {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setNome(usuarioSaveDto.nome());
        usuarioModel.setEmail(usuarioSaveDto.email());
        usuarioModel.setSenha(encoder.encode(usuarioSaveDto.senha()));
        usuarioModel.setCpf(usuarioSaveDto.cpf());
        usuarioModel.setAtivo(true);
        usuarioModel.setCep(usuarioSaveDto.cep());
        usuarioModel.setEndereco(usuarioSaveDto.endereco());
        usuarioModel.setNumero(usuarioSaveDto.numero());
        usuarioModel.setTelefone(usuarioSaveDto.telefone());
        usuarioModel.setAssinatura(false);
        usuarioModel.setComplemento(usuarioSaveDto.complemento());
        usuarioModel.setHoras_estudadas(0);

        SimuladorModel simuladorModel = new SimuladorModel();
        simuladorModel.setVolume_master(80);
        simuladorModel.setUsuarioModel(usuarioModel);
        usuarioModel.setSimuladorModel(simuladorModel);
        if (usuarioSaveDto.idInstrumento() != null) {
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioSaveDto.idInstrumento()).get();
            usuarioModel.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioSaveDto.idTipoUsuario()).get();
        usuarioModel.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public UsuarioModel updateUsuario(Long id, UsuarioSaveDto usuarioSaveDto) {
        UsuarioModel usuarioUpdate = usuarioRepository.findById(id).get();
        usuarioUpdate.setNome(usuarioSaveDto.nome());
        usuarioUpdate.setEmail(usuarioSaveDto.email());
        usuarioUpdate.setSenha(encoder.encode(usuarioSaveDto.senha()));
        usuarioUpdate.setCpf(usuarioSaveDto.cpf());
        usuarioUpdate.setCep(usuarioSaveDto.cep());
        usuarioUpdate.setEndereco(usuarioSaveDto.endereco());
        usuarioUpdate.setNumero(usuarioSaveDto.numero());
        usuarioUpdate.setTelefone(usuarioSaveDto.telefone());
        usuarioUpdate.setComplemento(usuarioSaveDto.complemento());

        if (usuarioSaveDto.idInstrumento() != null) {
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioSaveDto.idInstrumento()).get();
            usuarioUpdate.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioSaveDto.idTipoUsuario()).get();
        usuarioUpdate.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioUpdate);
    }

    @Transactional
    public UsuarioModel updateAtivo(Long id, Boolean ativo) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        usuarioModel.setAtivo(ativo);
        return usuarioRepository.save(usuarioModel);
    }
    @Transactional
    public UsuarioModel updateAssinatura(Long id, Boolean assinatura) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        usuarioModel.setAssinatura(assinatura);
        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}

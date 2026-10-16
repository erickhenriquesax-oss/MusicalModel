package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.UsuarioRequestDto;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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


    @Cacheable("usuarios")
    @Transactional
    public List<UsuarioModel> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public UsuarioModel findById(Long id) {
        return usuarioRepository.findById(id).get();
    }


    @CacheEvict(value = "usuarios", allEntries = true)
    @Transactional
    public UsuarioModel saveUsuario(UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setNome(usuarioRequestDto.nome());
        usuarioModel.setEmail(usuarioRequestDto.email());
        usuarioModel.setSenha(encoder.encode(usuarioRequestDto.senha()));
        usuarioModel.setCpf(usuarioRequestDto.cpf());
        usuarioModel.setAtivo(true);
        usuarioModel.setCep(usuarioRequestDto.cep());
        usuarioModel.setEndereco(usuarioRequestDto.endereco());
        usuarioModel.setNumero(usuarioRequestDto.numero());
        usuarioModel.setTelefone(usuarioRequestDto.telefone());
        usuarioModel.setAssinatura(false);
        usuarioModel.setComplemento(usuarioRequestDto.complemento());
        usuarioModel.setHoras_estudadas(0);

        SimuladorModel simuladorModel = new SimuladorModel();
        simuladorModel.setVolume_master(80);
        simuladorModel.setUsuarioModel(usuarioModel);
        usuarioModel.setSimuladorModel(simuladorModel);
        if (usuarioRequestDto.idInstrumento() != null) {
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioRequestDto.idInstrumento()).get();
            usuarioModel.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioRequestDto.idTipoUsuario()).get();
        usuarioModel.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioModel);
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    @Transactional
    public UsuarioModel updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioUpdate = usuarioRepository.findById(id).get();
        usuarioUpdate.setNome(usuarioRequestDto.nome());
        usuarioUpdate.setEmail(usuarioRequestDto.email());
        usuarioUpdate.setSenha(encoder.encode(usuarioRequestDto.senha()));
        usuarioUpdate.setCpf(usuarioRequestDto.cpf());
        usuarioUpdate.setCep(usuarioRequestDto.cep());
        usuarioUpdate.setEndereco(usuarioRequestDto.endereco());
        usuarioUpdate.setNumero(usuarioRequestDto.numero());
        usuarioUpdate.setTelefone(usuarioRequestDto.telefone());
        usuarioUpdate.setComplemento(usuarioRequestDto.complemento());

        if (usuarioRequestDto.idInstrumento() != null) {
            InstrumentoModel instrumentoModel = instrumentoRepository.findById(usuarioRequestDto.idInstrumento()).get();
            usuarioUpdate.setInstrumentoModel(instrumentoModel);
        }
        TipoUsuarioModel tipoUsuarioModel = tipoUsuarioRepository.findById(usuarioRequestDto.idTipoUsuario()).get();
        usuarioUpdate.setTipo_usuario(tipoUsuarioModel);

        return usuarioRepository.save(usuarioUpdate);
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    @Transactional
    public UsuarioModel updateAtivo(Long id, Boolean ativo) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        usuarioModel.setAtivo(ativo);
        return usuarioRepository.save(usuarioModel);
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    @Transactional
    public UsuarioModel updateAssinatura(Long id, Boolean assinatura) {
        UsuarioModel usuarioModel = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        usuarioModel.setAssinatura(assinatura);
        return usuarioRepository.save(usuarioModel);
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}

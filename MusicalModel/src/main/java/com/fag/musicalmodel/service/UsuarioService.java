package com.fag.musicalmodel.service;

import com.fag.musicalmodel.dto.request.UsuarioRequestDto;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
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
        return buscarUsuario(id);
    }


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
        simuladorModel.criarCanaisPadrao(); // 8 canais com todos os valores em 50
        usuarioModel.setSimuladorModel(simuladorModel);
        if (usuarioRequestDto.idInstrumento() != null) {
            usuarioModel.setInstrumentoModel(buscarInstrumento(usuarioRequestDto.idInstrumento()));
        }
        usuarioModel.setTipo_usuario(buscarTipoUsuario(usuarioRequestDto.idTipoUsuario()));

        return usuarioRepository.save(usuarioModel);
    }

    // A listagem de matrículas em cache guarda o nome do usuário: precisa ser limpa quando ele muda.
    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public UsuarioModel updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioUpdate = buscarUsuario(id);
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
            usuarioUpdate.setInstrumentoModel(buscarInstrumento(usuarioRequestDto.idInstrumento()));
        }
        usuarioUpdate.setTipo_usuario(buscarTipoUsuario(usuarioRequestDto.idTipoUsuario()));

        return usuarioRepository.save(usuarioUpdate);
    }

    @Transactional
    public UsuarioModel updateAtivo(Long id, Boolean ativo) {
        UsuarioModel usuarioModel = buscarUsuario(id);
        usuarioModel.setAtivo(ativo);
        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public UsuarioModel updateAssinatura(Long id, Boolean assinatura) {
        UsuarioModel usuarioModel = buscarUsuario(id);
        usuarioModel.setAssinatura(assinatura);
        return usuarioRepository.save(usuarioModel);
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
        usuarioRepository.flush(); // aplica já, para que um vínculo (ex.: matrícula) vire 409 tratado
    }

    private UsuarioModel buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    private InstrumentoModel buscarInstrumento(Long id) {
        return instrumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instrumento não encontrado: " + id));
    }

    private TipoUsuarioModel buscarTipoUsuario(Long id) {
        return tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado: " + id));
    }
}

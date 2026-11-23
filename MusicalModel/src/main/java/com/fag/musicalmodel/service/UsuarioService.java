package com.fag.musicalmodel.service;

import com.fag.musicalmodel.config.JWTUserData;
import com.fag.musicalmodel.dto.request.UsuarioRequestDto;
import com.fag.musicalmodel.enums.UserRole;
import com.fag.musicalmodel.exception.BusinessException;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.InstrumentoRepository;
import com.fag.musicalmodel.repository.TipoUsuarioRepository;
import com.fag.musicalmodel.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final SimuladorService simuladorService;
    @Autowired
    private PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          InstrumentoRepository instrumentoRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          SimuladorService simuladorService) {
        this.usuarioRepository = usuarioRepository;
        this.instrumentoRepository = instrumentoRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.simuladorService = simuladorService;
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
        usuarioModel.setEmail(normalizarEmail(usuarioRequestDto.email()));
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

        if (usuarioRequestDto.idInstrumento() != null) {
            usuarioModel.setInstrumentoModel(buscarInstrumento(usuarioRequestDto.idInstrumento()));
        }
        usuarioModel.setTipo_usuario(buscarTipoUsuario(UserRole.ALUNO.getCodigo()));

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuarioModel);

        usuarioSalvo.setSimuladorModel(simuladorService.criarSimulador(usuarioSalvo));
        return usuarioSalvo;
    }

    @Transactional
    public UsuarioModel definirInstrumento(Long idUsuario, Long idInstrumento, JWTUserData solicitante) {
        boolean admin = solicitante.isAdmin();
        if (!admin && !idUsuario.equals(solicitante.userId())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Você só pode definir o instrumento da sua própria conta");
        }
        UsuarioModel usuario = buscarUsuario(idUsuario);
        if (usuario.getInstrumentoModel() != null && !admin) {
            throw BusinessException.conflito("O instrumento já foi escolhido e não pode ser alterado");
        }
        usuario.setInstrumentoModel(buscarInstrumento(idInstrumento));
        return usuarioRepository.save(usuario);
    }

    @CacheEvict(value = "matriculas", allEntries = true)
    @Transactional
    public UsuarioModel updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto, JWTUserData solicitante) {
        UsuarioModel usuarioUpdate = buscarUsuario(id);
        usuarioUpdate.setNome(usuarioRequestDto.nome());
        usuarioUpdate.setEmail(normalizarEmail(usuarioRequestDto.email()));
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
        if (usuarioRequestDto.idTipoUsuario() != null && solicitante.isAdmin()) {
            usuarioUpdate.setTipo_usuario(buscarTipoUsuario(usuarioRequestDto.idTipoUsuario()));
        }

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
        usuarioRepository.flush();
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
        boolean tipoValido = Arrays.stream(UserRole.values()).anyMatch(role -> role.getCodigo().equals(id));
        if (!tipoValido) {
            throw new ResourceNotFoundException("Tipo de usuário inexistente");
        }
        return tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário inexistente"));
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}

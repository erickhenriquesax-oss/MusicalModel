package com.fag.musicalmodel.controller;

import com.fag.musicalmodel.config.TokenConfig;
import com.fag.musicalmodel.dto.request.LoginDto;
import com.fag.musicalmodel.dto.request.UsuarioRequestDto;
import com.fag.musicalmodel.dto.response.LoginResponse;
import com.fag.musicalmodel.dto.response.UsuarioResponseDto;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.UsuarioRepository;
import com.fag.musicalmodel.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/musicalmodel/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenConfig tokenConfig;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken usuarioAndSenha = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha()); //autentica usando email e senha
        Authentication authentication = authenticationManager.authenticate(usuarioAndSenha);

        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal(); //pegar o usuario
        String token = tokenConfig.generateToken(usuario); //gera o token
        return ResponseEntity.ok(new LoginResponse(token));
    }

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> findAll(){
        List<UsuarioResponseDto> list = usuarioService.findAll().stream()
                .map(UsuarioResponseDto::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> saveUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioSalvo = usuarioService.saveUsuario(usuarioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDto(usuarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioAtualizado = usuarioService.updateUsuario(id, usuarioRequestDto);
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioAtualizado));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/ativo")
    public ResponseEntity<UsuarioResponseDto> patchAtivo(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean ativo = payload.get("ativo");
        if (ativo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.updateAtivo(id, ativo)));
    }

    @PatchMapping("/{id}/assinatura")
    public ResponseEntity<UsuarioResponseDto> patchAssinatura(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean assinatura = payload.get("assinatura");
        if (assinatura == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.updateAssinatura(id, assinatura)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        usuarioService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

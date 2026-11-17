package com.fag.musicalmodel.controller;

import com.fag.musicalmodel.config.JWTUserData;
import com.fag.musicalmodel.config.TokenConfig;
import com.fag.musicalmodel.dto.request.LoginDto;
import com.fag.musicalmodel.dto.request.UsuarioRequestDto;
import com.fag.musicalmodel.dto.response.LoginResponse;
import com.fag.musicalmodel.dto.response.UsuarioResponseDto;
import com.fag.musicalmodel.exception.BusinessException;
import com.fag.musicalmodel.model.UsuarioModel;
import com.fag.musicalmodel.repository.UsuarioRepository;
import com.fag.musicalmodel.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/musicalmodel/usuario")
@Tag(name = "Usuário", description = "Cadastro, autenticação e gerenciamento de usuários")
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
    @SecurityRequirements
    @Operation(summary = "Realiza login", description = "Autentica com e-mail e senha e retorna o token JWT junto com o ID, o nome, o papel (ALUNO/ADMIN) e o instrumento do usuário. Não exige autenticação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida; retorna o token e os dados do usuário", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Usuário desativado")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken usuarioAndSenha = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha()); //autentica usando email e senha
        Authentication authentication = authenticationManager.authenticate(usuarioAndSenha);

        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal(); //pegar o usuario
        String token = tokenConfig.generateToken(usuario); //gera o token
        return ResponseEntity.ok(new LoginResponse(token, usuario));
    }

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Busca todos os usuários", description = "Lista todos os usuários cadastrados. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN")
    })
    public ResponseEntity<List<UsuarioResponseDto>> findAll(){
        List<UsuarioResponseDto> list = usuarioService.findAll().stream()
                .map(UsuarioResponseDto::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por ID", description = "Retorna os dados do usuário informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.findById(id)));
    }

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Cadastra usuário", description = "Cria um novo usuário, sempre como ALUNO (o idTipoUsuario enviado é ignorado). Também cria o simulador com 8 canais. Não exige autenticação.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "404", description = "Instrumento não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail ou CPF já cadastrado")
    })
    public ResponseEntity<UsuarioResponseDto> saveUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioModel usuarioSalvo = usuarioService.saveUsuario(usuarioRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDto(usuarioSalvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza usuário", description = "Atualiza os dados do usuário informado pelo ID. O idTipoUsuario só é aplicado quando quem chama é ADMIN; para os demais é ignorado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Usuário, instrumento ou tipo de usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail ou CPF já cadastrado para outro usuário")
    })
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto,
                                                            @AuthenticationPrincipal JWTUserData solicitante) {
        UsuarioModel usuarioAtualizado = usuarioService.updateUsuario(id, usuarioRequestDto, solicitante);
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioAtualizado));
    }

    @PutMapping("/{id}/instrumento/{idInstrumento}")
    @Operation(summary = "Define o instrumento do usuário", description = "Grava o instrumento gratuito escolhido pelo usuário (tela de seleção após o cadastro). Recebe apenas os IDs no caminho, sem corpo. A escolha é permanente: depois de definida, só um ADMIN pode alterá-la. O usuário só pode definir o instrumento da própria conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrumento definido", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Tentativa de definir o instrumento de outro usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário ou instrumento não encontrado"),
            @ApiResponse(responseCode = "409", description = "O instrumento já foi escolhido")
    })
    public ResponseEntity<UsuarioResponseDto> definirInstrumento(@PathVariable Long id, @PathVariable Long idInstrumento,
                                                                 @AuthenticationPrincipal JWTUserData solicitante) {
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.definirInstrumento(id, idInstrumento, solicitante)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativa ou desativa usuário", description = "Altera o campo ativo do usuário informado pelo ID. Disponível somente para ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"ativo\": true}"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status ativo atualizado", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Campo ativo ausente no corpo"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDto> patchAtivo(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean ativo = payload.get("ativo");
        if (ativo == null) {
            throw BusinessException.requisicaoInvalida("O campo 'ativo' (true ou false) é obrigatório");
        }
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.updateAtivo(id, ativo)));
    }

    @PatchMapping("/{id}/assinatura")
    @Operation(summary = "Atualiza assinatura do usuário", description = "Altera o campo assinatura do usuário informado pelo ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"assinatura\": true}"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assinatura atualizada", content = @Content(schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Campo assinatura ausente no corpo"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDto> patchAssinatura(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean assinatura = payload.get("assinatura");
        if (assinatura == null) {
            throw BusinessException.requisicaoInvalida("O campo 'assinatura' (true ou false) é obrigatório");
        }
        return ResponseEntity.ok(new UsuarioResponseDto(usuarioService.updateAssinatura(id, assinatura)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta usuário", description = "Deleta um usuário através do ID. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário excluído"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Usuário possui matrículas e não pode ser excluído")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        usuarioService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

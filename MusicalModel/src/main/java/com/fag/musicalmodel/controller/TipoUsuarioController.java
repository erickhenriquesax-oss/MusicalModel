package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.TipoUsuarioRequestDto;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.service.TipoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/tipousuario")
@Tag(name = "Tipo de Usuário", description = "Gerenciamento dos tipos (perfis) de usuário")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Busca todos os tipos de usuário", description = "Lista todos os tipos de usuário cadastrados. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tipos de usuário", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoUsuarioModel.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN")
    })
    public ResponseEntity<List<TipoUsuarioModel>> getAllTipoUsuario(){
        return ResponseEntity.status(HttpStatus.OK).body(tipoUsuarioService.getAllTipoUsuario());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Cria tipo de usuário", description = "Cadastra um novo tipo de usuário. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de usuário criado", content = @Content(schema = @Schema(implementation = TipoUsuarioModel.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "409", description = "Já existe um tipo de usuário com esta descrição")
    })
    public ResponseEntity<TipoUsuarioModel> saveTipoUsuario(@Valid @RequestBody TipoUsuarioRequestDto tipoUsuarioRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoUsuarioService.saveTipoUsuario(tipoUsuarioRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta tipo de usuário", description = "Deleta um tipo de usuário através do ID. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de usuário excluído; retorna mensagem de confirmação"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Tipo de usuário em uso por usuários")
    })
    public ResponseEntity<String> deleteTipoUsuario(@PathVariable Long id){
        tipoUsuarioService.deleteTipoUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tipo de Usuário Deletado");
    }
}

package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.InstrumentoRequestDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.service.InstrumentoService;
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
@RequestMapping("/musicalmodel/instrumentos")
@Tag(name = "Instrumento", description = "Gerenciamento dos instrumentos musicais")
public class InstrumentoController {

    private final InstrumentoService instrumentoService;

    public InstrumentoController(InstrumentoService instrumentoService) {
        this.instrumentoService = instrumentoService;
    }

    @GetMapping
    @Operation(summary = "Busca todos os instrumentos", description = "Lista todos os instrumentos cadastrados. Disponível para qualquer usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de instrumentos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InstrumentoModel.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<InstrumentoModel>> getAllInstrumentos(){
        return ResponseEntity.status(HttpStatus.OK).body(instrumentoService.getAllInstrumentos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Cria instrumento", description = "Cadastra um novo instrumento. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Instrumento criado", content = @Content(schema = @Schema(implementation = InstrumentoModel.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "409", description = "Já existe um instrumento com este nome ou descrição")
    })
    public ResponseEntity<InstrumentoModel> saveIntrumento(@Valid @RequestBody InstrumentoRequestDto instrumentoRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentoService.saveInstrumento(instrumentoRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta instrumento", description = "Deleta um instrumento através do ID. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrumento excluído; retorna mensagem de confirmação"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Instrumento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Instrumento em uso por cursos ou usuários")
    })
    public ResponseEntity<String> deleteInstrumento(@PathVariable Long id){
        instrumentoService.deleteInstrumento(id);
        return ResponseEntity.status(HttpStatus.OK).body("Instrumento deletado com sucesso");
    }
}

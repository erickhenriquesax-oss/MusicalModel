package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.CursoRequestDto;
import com.fag.musicalmodel.dto.response.CursoResponseDto;
import com.fag.musicalmodel.exception.BusinessException;
import com.fag.musicalmodel.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.util.Map;

@RestController
@RequestMapping("/musicalmodel/curso")
@Tag(name = "Curso", description = "Gerenciamento dos cursos disponíveis")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    @Operation(summary = "Busca todos os cursos", description = "Lista todos os cursos cadastrados, ativos e inativos, com o instrumento de cada um. Disponível para qualquer usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cursos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CursoResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<CursoResponseDto>> getAllCurso(){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.getAllCursos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Cria curso", description = "Cria um novo curso, sempre ativo. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Curso criado", content = @Content(schema = @Schema(implementation = CursoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido, campos obrigatórios ausentes ou acima do tamanho máximo"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Instrumento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Já existe um curso com este título")
    })
    public ResponseEntity<CursoResponseDto> addCurso(@Valid @RequestBody CursoRequestDto cursoRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.saveCurso(cursoRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idCurso}")
    @Operation(summary = "Atualiza curso", description = "Atualiza os dados de um curso através do ID. Para ativar ou desativar, use PATCH /{idCurso}/ativo. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso atualizado", content = @Content(schema = @Schema(implementation = CursoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido, campos obrigatórios ausentes ou acima do tamanho máximo"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso ou instrumento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Já existe outro curso com este título")
    })
    public ResponseEntity<CursoResponseDto> updateCurso(@PathVariable Long idCurso, @Valid @RequestBody CursoRequestDto cursoRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.updateCurso(idCurso, cursoRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{idCurso}/ativo")
    @Operation(summary = "Ativa ou desativa curso", description = "Altera o campo ativo do curso. Curso inativo não aceita novas matrículas. Disponível somente para ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"ativo\": false}"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status ativo atualizado", content = @Content(schema = @Schema(implementation = CursoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Campo ativo ausente no corpo"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<CursoResponseDto> patchAtivo(@PathVariable Long idCurso, @RequestBody Map<String, Boolean> payload){
        Boolean ativo = payload.get("ativo");
        if (ativo == null) {
            throw BusinessException.requisicaoInvalida("O campo 'ativo' (true ou false) é obrigatório");
        }
        return ResponseEntity.ok(cursoService.updateAtivo(idCurso, ativo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idCurso}")
    @Operation(summary = "Deleta curso", description = "Deleta um curso através do ID. Cursos com matrículas não podem ser excluídos; desative-os. Disponível somente para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso excluído"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
            @ApiResponse(responseCode = "409", description = "Curso possui matrículas e não pode ser excluído")
    })
    public void deleteCurso(@PathVariable Long idCurso){
        cursoService.deleteCurso(idCurso);
    }
}

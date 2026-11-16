package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.MatriculaRequestDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.dto.response.MatriculaResponseDto;
import com.fag.musicalmodel.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/musicalmodel/matricula")
@Tag(name = "Matrícula", description = "Gerenciamento das matrículas de usuários em cursos")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    @Operation(summary = "Busca todas as matrículas", description = "Lista todas as matrículas cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de matrículas", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatriculaResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<MatriculaResponseDto>> getAllMatriculas() {
        return new ResponseEntity<>(matriculaService.getAllMatriculas(), HttpStatus.OK);
    }

    @GetMapping("/curso/{idCurso}")
    @Operation(summary = "Busca matrículas por curso", description = "Lista as matrículas vinculadas ao curso informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de matrículas do curso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatriculaResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<MatriculaResponseDto>> getMatriculasPorCurso(@PathVariable Long idCurso){
        return ResponseEntity.ok(matriculaService.getMatriculaByIdCurso(idCurso));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Busca matrículas por usuário", description = "Lista as matrículas do usuário informado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de matrículas do usuário", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatriculaResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<MatriculaResponseDto>> getMatriculasPorUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(matriculaService.getMatriculaByIdUsuario(idUsuario));
    }

    @PostMapping
    @Operation(summary = "Cria matrícula", description = "Matricula um usuário em um curso. O usuário e o curso precisam estar ativos, e não pode existir outra matrícula ATIVA do mesmo usuário no mesmo curso.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Matrícula criada", content = @Content(schema = @Schema(implementation = MatriculaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Usuário ou curso não encontrado"),
            @ApiResponse(responseCode = "409", description = "Usuário desativado, curso inativo ou matrícula ativa já existente")
    })
    public ResponseEntity<MatriculaResponseDto> saveMatricula(@Valid @RequestBody MatriculaRequestDto matriculaRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.saveMatricula(matriculaRequestDto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza status da matrícula", description = "Altera o status de uma matrícula através do ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status da matrícula atualizado", content = @Content(schema = @Schema(implementation = MatriculaResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Status ausente ou inválido (aceitos: ATIVA, CONCLUIDA, CANCELADA, TRANCADA)"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Matrícula não encontrada"),
            @ApiResponse(responseCode = "409", description = "Reativação geraria duas matrículas ativas no mesmo curso")
    })
    public ResponseEntity<MatriculaResponseDto> atualizarStatus(@PathVariable Long id, @Parameter(description = "Novo status da matrícula") @RequestParam StatusMatricula status){
        MatriculaResponseDto matricula = matriculaService.atualizarStatus(id, status);
        return ResponseEntity.ok(matricula);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta matrícula", description = "Remove uma matrícula através do ID. Disponível somente para ADMIN. Para o aluno encerrar a matrícula, use a alteração de status.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matrícula excluída"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Operação permitida somente para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Matrícula não encontrada")
    })
    public ResponseEntity<Void> deleteMatricula(@PathVariable Long id){
        matriculaService.deleteMatricula(id);
        return ResponseEntity.noContent().build();
    }
}

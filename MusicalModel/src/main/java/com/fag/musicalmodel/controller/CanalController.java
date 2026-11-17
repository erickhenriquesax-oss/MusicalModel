package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.CanalNomeRequestDto;
import com.fag.musicalmodel.dto.request.CanalRequestDto;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.service.CanalService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/canal")
@Tag(name = "Canal", description = "Canais da mesa de som do simulador. Cada simulador tem 8 canais, criados no cadastro do usuário com todos os valores em 50.")
public class CanalController {

    private final CanalService canalService;

    public CanalController(CanalService canalService) {
        this.canalService = canalService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca canais por simulador", description = "Lista os canais do simulador informado pelo ID, ordenados pelo ID do canal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de canais do simulador (vazia se o simulador não existir)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CanalModel.class)))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
    })
    public ResponseEntity<List<CanalModel>> getCanalsBySimulador(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(canalService.findCanalByIdSimulador(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ajusta canal", description = "Grava agudo, médio, grave e volume de um canal através do ID. Todos os valores são obrigatórios e devem estar entre 0 e 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canal atualizado", content = @Content(schema = @Schema(implementation = CanalModel.class))),
            @ApiResponse(responseCode = "400", description = "Campo ausente ou valor fora da faixa de 0 a 100"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Canal não encontrado")
    })
    public ResponseEntity<CanalModel> update(@PathVariable Long id, @Valid @RequestBody CanalRequestDto canalRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(canalService.updateCanal(id, canalRequestDto));
    }

    @PatchMapping("/{id}/nome")
    @Operation(summary = "Renomeia canal", description = "Altera o nome (descrição) de um canal, ex.: \"Voz principal\". O nome fica gravado e volta nas próximas sessões do usuário.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"nome\": \"Voz principal\"}"))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canal renomeado", content = @Content(schema = @Schema(implementation = CanalModel.class))),
            @ApiResponse(responseCode = "400", description = "Nome ausente, em branco ou com mais de 30 caracteres"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Canal não encontrado")
    })
    public ResponseEntity<CanalModel> renomear(@PathVariable Long id, @Valid @RequestBody CanalNomeRequestDto canalNomeRequestDto) {
        return ResponseEntity.ok(canalService.renomearCanal(id, canalNomeRequestDto));
    }
}

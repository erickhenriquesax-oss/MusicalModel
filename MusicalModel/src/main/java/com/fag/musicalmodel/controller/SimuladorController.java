package com.fag.musicalmodel.controller;

import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.service.SimuladorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musicalmodel/simulador")
@Tag(name = "Simulador", description = "Simulador musical vinculado ao usuário")
public class SimuladorController {

    private final SimuladorService simuladorService;


    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca simulador por usuário", description = "Retorna o simulador vinculado ao usuário informado pelo ID (ID do usuário), com os seus 8 canais.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulador do usuário", content = @Content(schema = @Schema(implementation = SimuladorModel.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Simulador não encontrado para o usuário")
    })
    public ResponseEntity<SimuladorModel> getSimuladorByIdUsuario(@PathVariable Long id) {
        SimuladorModel simulador = simuladorService.getSimuladorByUsuarioModel_IdUsuario(id);
        return ResponseEntity.ok(simulador);
    }

    @PatchMapping("/{id}/volume-master")
    @Operation(summary = "Atualiza volume master", description = "Altera o volume master do simulador informado pelo ID (ID do simulador, não do usuário). O volume deve estar entre 0 e 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Volume master atualizado", content = @Content(schema = @Schema(implementation = SimuladorModel.class))),
            @ApiResponse(responseCode = "400", description = "Volume ausente, não numérico ou fora da faixa de 0 a 100"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "404", description = "Simulador não encontrado")
    })
    public ResponseEntity<SimuladorModel> updateVolumeMaster(@PathVariable Long id, @Parameter(description = "Novo volume master, de 0 a 100") @RequestParam
                                                             @NotNull @Min(value = 0, message = "O volume deve estar entre 0 e 100")
                                                             @Max(value = 100, message = "O volume deve estar entre 0 e 100") Integer volume) {
        return ResponseEntity.ok(simuladorService.updateVolumeMaster(id, volume));
    }
}

package com.fag.musicalmodel.controller;

import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.service.SimuladorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musicalmodel/simulador")
public class SimuladorController {

    private final SimuladorService simuladorService;


    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimuladorModel> getSimuladorByIdUsuario(@PathVariable Long id) {
        SimuladorModel simulador = simuladorService.getSimuladorByUsuarioModel_IdUsuario(id);
        return ResponseEntity.ok(simulador);
    }

    @PatchMapping("/{id}/volume-master")
    public ResponseEntity<SimuladorModel> updateVolumeMaster(@PathVariable Long id, @RequestParam Integer volume) {
        return ResponseEntity.ok(simuladorService.updateVolumeMaster(id, volume));
    }
}

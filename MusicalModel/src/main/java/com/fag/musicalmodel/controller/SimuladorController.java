package com.fag.musicalmodel.controller;

import com.fag.musicalmodel.model.SimuladorModel;
import com.fag.musicalmodel.service.SimuladorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/musicalmodel/simulador")
public class SimuladorController {

    private final SimuladorService simuladorService;


    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimuladorModel> getSimuladorByIdUsuario(@PathVariable Long id) {
        simuladorService.getSimuladorByUsuarioModel_IdUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body(simuladorService.getSimuladorByUsuarioModel_IdUsuario(id));
    }
}

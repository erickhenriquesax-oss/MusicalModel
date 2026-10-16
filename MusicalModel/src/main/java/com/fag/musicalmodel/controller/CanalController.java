package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.CanalRequestDto;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.service.CanalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/canal")
public class CanalController {

    private final CanalService canalService;

    public CanalController(CanalService canalService) {
        this.canalService = canalService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CanalModel>> getCanalsBySimulador(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(canalService.findCanalByIdSimulador(id));
    }

    @PostMapping
    public ResponseEntity<CanalModel> save(@Valid @RequestBody CanalRequestDto canalRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(canalService.saveCanal(canalRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanalModel> update(@PathVariable Long id, @Valid @RequestBody CanalRequestDto canalRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(canalService.updateCanal(id, canalRequestDto));
    }
}

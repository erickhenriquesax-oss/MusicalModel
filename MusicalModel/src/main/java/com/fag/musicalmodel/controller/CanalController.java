package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.CanalDto;
import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.service.CanalService;
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
    public List<CanalModel> getCanalsBySimulador(@PathVariable Long id) {
        return canalService.findCanalByIdSimulador(id);
    }

    @PostMapping
    public ResponseEntity<CanalModel> save(@RequestBody CanalDto canalDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(canalService.saveCanal(canalDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanalModel> update(@PathVariable Long id,  @RequestBody CanalDto canalDto) {
        return ResponseEntity.status(HttpStatus.OK).body(canalService.updateCanal(id, canalDto));
    }
}

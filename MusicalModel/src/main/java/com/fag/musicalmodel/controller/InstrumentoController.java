package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.InstrumentoRequestDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.service.InstrumentoService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/instrumentos")
public class InstrumentoController {

    private final InstrumentoService instrumentoService;

    public InstrumentoController(InstrumentoService instrumentoService) {
        this.instrumentoService = instrumentoService;
    }

    @GetMapping
    public ResponseEntity<List<InstrumentoModel>> getAllInstrumentos(){
        return ResponseEntity.status(HttpStatus.OK).body(instrumentoService.getAllInstrumentos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InstrumentoModel> saveIntrumento(@Valid @RequestBody InstrumentoRequestDto instrumentoRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentoService.saveInstrumento(instrumentoRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstrumento(@PathVariable Long id){
        instrumentoService.deleteInstrumento(id);
        return ResponseEntity.status(HttpStatus.OK).body("Instrumento deletado com sucesso");
    }
}

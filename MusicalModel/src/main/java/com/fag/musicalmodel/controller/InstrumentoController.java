package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.InstrumentoDto;
import com.fag.musicalmodel.model.InstrumentoModel;
import com.fag.musicalmodel.service.InstrumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<InstrumentoModel> saveIntrumento(@RequestBody InstrumentoDto instrumentoDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentoService.saveInstrumento(instrumentoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstrumento(@PathVariable Long id){
        instrumentoService.deleteInstrumento(id);
        return ResponseEntity.status(HttpStatus.OK).body("Instrumento deletado com sucesso");
    }
}

package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.MatriculaRequestDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.MatriculaModel;
import com.fag.musicalmodel.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/matricula")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public ResponseEntity<List<MatriculaModel>> getAllMatriculas() {
        return new ResponseEntity<>(matriculaService.getAllMatriculas(), HttpStatus.OK);
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<MatriculaModel>> getMatriculasPorCurso(@PathVariable Long idCurso){
        return ResponseEntity.ok(matriculaService.getMatriculaByIdCurso(idCurso));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MatriculaModel>> getMatriculasPorUsuario(@PathVariable Long idUsuario){
        return ResponseEntity.ok(matriculaService.getMatriculaByIdUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<MatriculaModel> saveMatricula(@Valid @RequestBody MatriculaRequestDto matriculaRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.saveMatricula(matriculaRequestDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MatriculaModel> atualizarStatus(@PathVariable Long id, @RequestParam StatusMatricula status){
        MatriculaModel matricula = matriculaService.atualizarStatus(id, status);
        return ResponseEntity.ok(matricula);
    }
}

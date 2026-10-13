package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.MatriculaDto;
import com.fag.musicalmodel.enums.StatusMatricula;
import com.fag.musicalmodel.model.MatriculaModel;
import com.fag.musicalmodel.service.MatriculaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musicalmodel/matricula")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping("/{id}/usuario")
    public ResponseEntity<MatriculaModel> getMatriculaByIdUsuario(@PathVariable Long id){
        MatriculaModel matriculaModel = matriculaService.getMatriculaByUsuarioModel_IdUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body(matriculaModel);
    }

    @GetMapping("/{id}/curso")
    public ResponseEntity<MatriculaModel> getMatriculaByIdCurso(@PathVariable Long id){
        MatriculaModel matriculaModel = matriculaService.getMatriculaByIdCurso(id).stream().toList().get(0);
        return ResponseEntity.status(HttpStatus.OK).body(matriculaModel);
    }

    @PostMapping
    public ResponseEntity<MatriculaModel> saveMatricula(@RequestBody MatriculaDto matriculaDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.saveMatricula(matriculaDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MatriculaModel> atualizarStatus(@PathVariable Long id, @RequestParam StatusMatricula status){
        MatriculaModel matricula = matriculaService.atualizarStatus(id, status);
        return ResponseEntity.ok(matricula);
    }
}

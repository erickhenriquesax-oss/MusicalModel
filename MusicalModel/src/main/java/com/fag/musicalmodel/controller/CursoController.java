package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.CursoDto;
import com.fag.musicalmodel.model.CursoModel;
import com.fag.musicalmodel.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/curso")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<CursoModel>> getAllCurso(){
        cursoService.getAllCursos();
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.getAllCursos());
    }

    @PostMapping
    public ResponseEntity<CursoModel> addCurso(@RequestBody CursoDto cursoDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.saveCurso(cursoDto));
    }

    @PutMapping
    public ResponseEntity<CursoModel> updateCurso(@PathVariable Long idCurso, @RequestBody CursoDto cursoDto){
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.updateCurso(idCurso, cursoDto));
    }

    @DeleteMapping
    public void deleteCurso(@PathVariable Long idCurso){
        cursoService.deleteCurso(idCurso);
    }
}

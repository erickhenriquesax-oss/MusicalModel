package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.TipoUsuarioDto;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.service.TipoUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/tipousuario")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @GetMapping
    public ResponseEntity<List<TipoUsuarioModel>> getAllTipoUsuario(){
        return ResponseEntity.status(HttpStatus.OK).body(tipoUsuarioService.getAllTipoUsuario());
    }

    @PostMapping
    public ResponseEntity<TipoUsuarioModel> saveTipoUsuario(@RequestBody TipoUsuarioDto tipoUsuarioDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoUsuarioService.saveTipoUsuario(tipoUsuarioDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTipoUsuario(@PathVariable Long id){
        tipoUsuarioService.deleteTipoUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tipo de Usuário Deletado");
    }
}

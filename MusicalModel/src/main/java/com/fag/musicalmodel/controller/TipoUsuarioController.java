package com.fag.musicalmodel.controller;


import com.fag.musicalmodel.dto.request.TipoUsuarioRequestDto;
import com.fag.musicalmodel.model.TipoUsuarioModel;
import com.fag.musicalmodel.service.TipoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musicalmodel/tipousuario")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoUsuarioModel>> getAllTipoUsuario(){
        return ResponseEntity.status(HttpStatus.OK).body(tipoUsuarioService.getAllTipoUsuario());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TipoUsuarioModel> saveTipoUsuario(@Valid @RequestBody TipoUsuarioRequestDto tipoUsuarioRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoUsuarioService.saveTipoUsuario(tipoUsuarioRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTipoUsuario(@PathVariable Long id){
        tipoUsuarioService.deleteTipoUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tipo de Usuário Deletado");
    }
}

package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
}

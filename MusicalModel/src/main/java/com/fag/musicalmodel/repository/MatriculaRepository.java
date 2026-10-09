package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {
}

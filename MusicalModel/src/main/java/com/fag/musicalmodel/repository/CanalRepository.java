package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.CanalModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanalRepository extends JpaRepository<CanalModel, Long> {

    List<CanalModel> findBySimuladorModel_IdSimuladorOrderByIdCanalAsc(Long id);

}

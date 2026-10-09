package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.CanalModel;
import com.fag.musicalmodel.model.SimuladorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CanalRepository extends JpaRepository<CanalModel, Long> {

    CanalModel findCanalModelByIdCanal(Long id);


}

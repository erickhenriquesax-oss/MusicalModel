package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.InstrumentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentoRepository extends JpaRepository<InstrumentoModel, Long> {

    InstrumentoModel findInstrumentoModelByIdInstrumento(Long id);

    InstrumentoModel findInstrumentoModelByNome(String nome);
}

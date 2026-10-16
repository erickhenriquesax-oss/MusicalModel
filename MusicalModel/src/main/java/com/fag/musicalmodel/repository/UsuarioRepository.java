package com.fag.musicalmodel.repository;

import com.fag.musicalmodel.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UserDetails> findUserByEmail(String username);

}

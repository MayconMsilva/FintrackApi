package com.project.fintrackApi.repository;

import com.project.fintrackApi.domain.Usuario;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


    boolean existsByEmail(String email);


}

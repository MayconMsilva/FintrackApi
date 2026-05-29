package com.project.fintrackApi.repository;


import com.project.fintrackApi.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByUsuarioEmail(String email);

    Optional<Conta> findByIdAndUsuarioEmail(Long id, String email);
}

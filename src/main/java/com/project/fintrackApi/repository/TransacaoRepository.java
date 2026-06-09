package com.project.fintrackApi.repository;

import com.project.fintrackApi.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {


    // Todas as Transações de uma conta
    List<Transacao> findByContaIdOrderByDataTransacaoDesc(Long contaId);

    // Todas as transações do usuário em todas as contas
    List<Transacao> findByContaUsuarioEmailOrderByDataTransacaoDesc(String email);
}

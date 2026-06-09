package com.project.fintrackApi.service;


import com.project.fintrackApi.domain.Conta;
import com.project.fintrackApi.domain.Transacao;
import com.project.fintrackApi.domain.enums.TipoTransacao;
import com.project.fintrackApi.dto.TransacaoRequestDTO;
import com.project.fintrackApi.dto.TransacaoResponseDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.exception.SaldoInsuficienteException;
import com.project.fintrackApi.repository.ContaRepository;
import com.project.fintrackApi.repository.TransacaoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.SaslException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {


    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    @Transactional
    public TransacaoResponseDTO criar(TransacaoRequestDTO dto, String emailUsuario){

        // Validação da conta de Origem pertence ao Usuário Autenticado
        Conta contaOrigem = contaRepository
                .findByIdAndUsuarioEmail(dto.getContaOrigemId(), emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Conta Não Encontrada"));

        return switch (dto.getTipoTransacao()){
            case RECEITA -> processarReceita(dto, contaOrigem);
            case DESPESA -> processarDespesa(dto, contaOrigem);
            case TRANSFERENCIA -> processarTransferencia(dto, contaOrigem, emailUsuario);
        };

    }


    // =====================================
    // RECEITA
    // =====================================
    private TransacaoResponseDTO processarReceita(TransacaoRequestDTO dto, Conta contaOrigem){

        contaOrigem.setSaldo(contaOrigem.getSaldo().add(dto.getValor()));
        contaRepository.save(contaOrigem);


        Transacao transacao = Transacao.builder()
                .valor(dto.getValor())
                .tipoTransacao(TipoTransacao.RECEITA)
                .descricao(dto.getDescricao())
                .conta(contaOrigem)
                .build();

        return toResponseDTO(transacaoRepository.save(transacao));
    }

    // =====================================
    // DESPESA
    // =====================================
    private TransacaoResponseDTO processarDespesa(TransacaoRequestDTO dto, Conta contaOrigem){

        // A Regra de Negócio - Saldo Insuficiente
        if(contaOrigem.getSaldo().compareTo(dto.getValor()) < 0){
            throw new SaldoInsuficienteException(
                    "Saldo Insuficiente. Saldo Atual: R$ " + contaOrigem.getSaldo()
            );
        }


        // Atualiza Saldo
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.getValor()));
        contaRepository.save(contaOrigem);

        Transacao transacao = Transacao.builder()
                .valor(dto.getValor())
                .tipoTransacao(TipoTransacao.DESPESA)
                .descricao(dto.getDescricao())
                .conta(contaOrigem)
                .build();

        return toResponseDTO(transacaoRepository.save(transacao));
    }


    // ================================================
    // TRANSFERENCIA
    // ================================================
    private TransacaoResponseDTO processarTransferencia(
            TransacaoRequestDTO dto,
            Conta contaOrigem,
            String emailUsuario){

        // Se contaDestino foi Informado
        if (dto.getContaDestinoId() == null){
            throw new IllegalArgumentException("Conta Destino é Obrigatória para a TransferÊncia");
        }


        // Se não está transferindo para a mesma Conta
        if (dto.getContaOrigemId().equals(dto.getContaDestinoId())){
            throw new IllegalArgumentException("Conta Origem e Destino Não Podem ser Iguais");
        }

        // Vai buscar Conta Destino - Deve pertencer ao mesmo usuário
        Conta contaDestino = contaRepository
                .findByIdAndUsuarioEmail(dto.getContaDestinoId(), emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Conta Destino Não Encontrada"));


        // Valida o Saldo antes de qualquer operação
        if (contaOrigem.getSaldo().compareTo(dto.getValor()) < 0 ){

            throw new SaldoInsuficienteException(
                    "Saldo Insuficiente. Saldo Atualo: R$ " + contaOrigem.getSaldo()
            );
        }


        // Vai Atualizar os Dois Saldos
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.getValor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(dto.getValor()));
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Transação Saída
        Transacao transacaoSaida = Transacao.builder()
                .valor(dto.getValor())
                .tipoTransacao(TipoTransacao.TRANSFERENCIA)
                .descricao("TransaferÊncia Para: " + contaDestino.getNome())
                .conta(contaOrigem)
                .build();

        Transacao saida = transacaoRepository.save(transacaoSaida);

        // Transação de Entrada - na conta destino
        Transacao transacaoEntrada = Transacao.builder()
                .valor(dto.getValor())
                .tipoTransacao(TipoTransacao.TRANSFERENCIA)
                .descricao("Transaferência De: " + contaOrigem.getNome())
                .transacaoOrigemId(saida.getId())
                .build();

        transacaoRepository.save(transacaoEntrada);

        // Retorna a transacao de saída como resposta principal
        return toResponseDTO(saida);

    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorConta(Long contaId, String emailUsuario){


        contaRepository.findByIdAndUsuarioEmail(contaId, emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Conta Não Encontrada"));

        return transacaoRepository
                .findByContaIdOrderByDataTransacaoDesc(contaId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarTodas(String emailUsuario){
        return transacaoRepository
                .findByContaUsuarioEmailOrderByDataTransacaoDesc(emailUsuario)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void cancelar(Long id, String emailUsuario) {


        Transacao transacao = transacaoRepository
                .findByIdAndConta_Usuario_Email(id, emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Transação Não Encontrada"));


        Conta conta = transacao.getConta();

        switch (transacao.getTipoTransacao()){
            case RECEITA -> conta.setSaldo(conta.getSaldo().subtract(transacao.getValor()));
            case DESPESA -> conta.setSaldo(conta.getSaldo().add(transacao.getValor()));
        }





        contaRepository.save(conta);

        transacaoRepository.delete(transacao);



    }


    // =================================================
    // MAPPER
    // =================================================
    private TransacaoResponseDTO toResponseDTO(Transacao transacao){
        return TransacaoResponseDTO.builder()
                .id(transacao.getId())
                .valor(transacao.getValor())
                .tipoTransacao(transacao.getTipoTransacao())
                .descricao(transacao.getDescricao())
                .contaId(transacao.getConta().getId())
                .nomeConta(transacao.getConta().getNome())
                .transacaoOrigemId(transacao.getTransacaoOrigemId())
                .dataTransacao(transacao.getDataTransacao())
                .build();
    }
}



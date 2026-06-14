package com.project.fintrackApi;


import com.project.fintrackApi.domain.Conta;
import com.project.fintrackApi.domain.Transacao;
import com.project.fintrackApi.domain.enums.TipoTransacao;
import com.project.fintrackApi.dto.TransacaoRequestDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.exception.SaldoInsuficienteException;
import com.project.fintrackApi.repository.ContaRepository;
import com.project.fintrackApi.repository.TransacaoRepository;
import com.project.fintrackApi.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.IllegalFormatException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private Conta contaComSaldo;
    private TransacaoRequestDTO dto;


    @BeforeEach
    void setUp(){
        contaComSaldo = Conta.builder()
                .id(2L)
                .nome("Bradesco")
                .saldo(new BigDecimal("1000.00"))
                .build();

        dto = new TransacaoRequestDTO();
        dto.setContaOrigemId(2L);
        dto.setDescricao("Teste");

    }


    // ===========================================================
    // TESTES DE RECEITA
    // ===========================================================
    @Test
    @DisplayName("Deve Aumentar Saldo Quando Criar Receita")
    void deve_aumentar_saldo_quando_criar_receita(){


        dto.setValor(new BigDecimal("500.00"));
        dto.setTipoTransacao(TipoTransacao.RECEITA);

        when(contaRepository.findByIdAndUsuarioEmail(any(), any()))
                .thenReturn(Optional.of(contaComSaldo));


        when(transacaoRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transacaoService.criar(dto, "gabriel@gmail.com");

        assertEquals(new BigDecimal("1500.00"), contaComSaldo.getSaldo());
    }

    @Test
    @DisplayName("Deve Diminuir Saldo Quando Criar Despesa Válida")
    void deve_diminuir_saldo_quando_criar_despesa_valida(){

        dto.setValor(new BigDecimal("300.00"));
        dto.setTipoTransacao(TipoTransacao.DESPESA);

        when(contaRepository.findByIdAndUsuarioEmail(any(), any()))
                .thenReturn(Optional.of(contaComSaldo));

        when(transacaoRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        transacaoService.criar(dto,"gabriel@gmail.com");

        assertEquals(new BigDecimal("700.00"), contaComSaldo.getSaldo());
    }

    @Test
    @DisplayName("Deve Lançar SaldoInsuficienteException Quando Saldo Insuficiente")
    void deve_lancar_exception_quando_saldo_insuficiente(){

        dto.setValor(new BigDecimal("2000.00"));
        dto.setTipoTransacao(TipoTransacao.DESPESA);

        when(contaRepository.findByIdAndUsuarioEmail(any(), any()))
                .thenReturn(Optional.of(contaComSaldo));

        assertThrows(SaldoInsuficienteException.class, () -> transacaoService.criar(dto, "gabriel@gmail.com"));

        assertEquals(new BigDecimal("1000.00"), contaComSaldo.getSaldo());


        verify(transacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve Lançar RecourceNotFoundException Quando Conta Não Encontrada")
    void deve_lancar_exception_quando_conta_nao_encontrada(){

        dto.setValor(new BigDecimal("1000.00"));
        dto.setTipoTransacao(TipoTransacao.DESPESA);


        when(contaRepository.findByIdAndUsuarioEmail(any(), any()))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> transacaoService.criar(dto, "gabriel@gmail.com"));
    }


    // =======================================================================
    // TESTES DE TRANSFERENCIA
    // =======================================================================

    @Test
    @DisplayName("Deve Transferir Valor Entre Contas Corretamente")
    void deve_transferir_valor_entre_contas_corretamente(){


        Conta contaDestino = Conta.builder()
                .id(3L)
                .nome("Santander")
                .saldo(new BigDecimal("200.00"))
                .build();

        dto.setValor(new BigDecimal("400.00"));
        dto.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
        dto.setContaDestinoId(3L);


        when(contaRepository.findByIdAndUsuarioEmail(2L, "gabriel@gmail.com"))
                .thenReturn(Optional.of(contaComSaldo));

        when(contaRepository.findByIdAndUsuarioEmail(3L, "gabriel@gmail.com"))
                .thenReturn(Optional.of(contaDestino));

        when(transacaoRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));


        transacaoService.criar(dto, "gabriel@gmail.com");

        assertEquals(new BigDecimal("600.00"), contaComSaldo.getSaldo());
        assertEquals(new BigDecimal("600.00"), contaDestino.getSaldo());
    }

    @Test
    @DisplayName("Deve Lançar Exception Quando Transferir Para a Mesma COnta")
    void deve_lancar_exception_quando_transferir_para_mesma_conta(){

        dto.setValor(new BigDecimal("100.00"));
        dto.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
        dto.setContaOrigemId(2L);
        dto.setContaDestinoId(2L);

        when(contaRepository.findByIdAndUsuarioEmail(any(), any()))
                .thenReturn(Optional.of(contaComSaldo));

        assertThrows(IllegalArgumentException.class, () -> transacaoService.criar(dto, "gabriel@gmail.com"));
    }


    @Test
    @DisplayName("Deve Reverter Saldo Quando Cancelar Despesa")
    void deve_reverter_saldo_quando_cancelar_despesa(){

        Conta conta = Conta.builder()
                .id(2L)
                .nome("Bradesco")
                .saldo(new BigDecimal("800.00"))
                .build();

        Transacao transacao = Transacao.builder()
                .id(2L)
                .valor(new BigDecimal("200.00"))
                .tipoTransacao(TipoTransacao.DESPESA)
                .conta(conta)
                .build();

        when(transacaoRepository.findByIdAndConta_Usuario_Email(any(), any()))
                .thenReturn(Optional.of(transacao));

        transacaoService.cancelar(2L, "gabriel@gmail.com");

        assertEquals(new BigDecimal("1000.00"), conta.getSaldo());

        verify(transacaoRepository).delete(transacao);

        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve Lançar Saldo Quando Cancelar Receita")
    void  deve_reverter_saldo_quando_cancelar_receita(){

        Conta conta = Conta.builder()
                .id(2L)
                .nome("Bradesco")
                .saldo(new BigDecimal("1500.00"))
                .build();

        Transacao transacao = Transacao.builder()
                .id(2L)
                .valor(new BigDecimal("500.00"))
                .tipoTransacao(TipoTransacao.RECEITA)
                .conta(conta)
                .build();

        when(transacaoRepository.findByIdAndConta_Usuario_Email(any(), any()))
                .thenReturn(Optional.of(transacao));

        transacaoService.cancelar(2L, "gabriel@gmail.com");

        assertEquals(new BigDecimal("1000.00"), conta.getSaldo());

        verify(transacaoRepository).delete(transacao);

        verify(contaRepository).save(conta);
    }



}

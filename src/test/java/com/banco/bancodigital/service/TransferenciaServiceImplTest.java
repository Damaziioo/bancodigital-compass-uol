package com.banco.bancodigital.service;

import com.banco.bancodigital.domain.entity.Conta;
import com.banco.bancodigital.domain.entity.Transferencia;
import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.request.RealizarTransferenciaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;
import com.banco.bancodigital.dto.response.TransferenciaResponse;
import com.banco.bancodigital.exception.ContaNaoEncontradaException;
import com.banco.bancodigital.exception.SaldoInsuficienteException;
import com.banco.bancodigital.exception.TransferenciaMesmaContaException;
import com.banco.bancodigital.repository.ContaRepository;
import com.banco.bancodigital.repository.TransferenciaRepository;
import com.banco.bancodigital.service.impl.ContaServiceImpl;
import com.banco.bancodigital.service.impl.NotificacaoServiceImpl;
import com.banco.bancodigital.service.impl.TransferenciaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceImplTest {
    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private NotificacaoServiceImpl notificacaoService;
    
    @InjectMocks
    private TransferenciaServiceImpl transferenciaService;

    @InjectMocks
    private ContaServiceImpl contaService;

    @Test
    void deveRealizarTransferenciaComSucesso() {
        // arrange
        UUID idOrigem = UUID.randomUUID();
        UUID idDestino = UUID.randomUUID();

        Conta contaOrigem = Conta.builder()
                .id(idOrigem)
                .nome("João")
                .saldo(new BigDecimal("1000.00"))
                .build();

        Conta contaDestino = Conta.builder()
                .id(idDestino)
                .nome("Maria")
                .saldo(new BigDecimal("500.00"))
                .build();

        RealizarTransferenciaRequest request = new RealizarTransferenciaRequest(
                idOrigem, idDestino, new BigDecimal("200.00"));

        Transferencia transferencia = Transferencia.builder()
                .id(UUID.randomUUID())
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .valor(new BigDecimal("200.00"))
                .dataHora(LocalDateTime.now())
                .build();

        when(contaRepository.findByIdWithLock(idOrigem)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(idDestino)).thenReturn(Optional.of(contaDestino));
        when(transferenciaRepository.saveAndFlush(any(Transferencia.class))).thenReturn(transferencia);

        TransferenciaResponse response = transferenciaService.transferir(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("800.00"), contaOrigem.getSaldo());
        assertEquals(new BigDecimal("700.00"), contaDestino.getSaldo());
        verify(contaRepository, times(1)).save(contaOrigem);
        verify(contaRepository, times(1)).save(contaDestino);
        verify(notificacaoService, times(1)).notificar(any(Transferencia.class));
    }

    @Test
    void deveLancarExcecaoQuandoTransferenciaParaMesmaConta() {
        UUID id = UUID.randomUUID();
        RealizarTransferenciaRequest request = new RealizarTransferenciaRequest(
                id, id, new BigDecimal("200.00"));

        assertThrows(TransferenciaMesmaContaException.class, () -> transferenciaService.transferir(request));
        verify(contaRepository, never()).findByIdWithLock(any());
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        UUID idOrigem = UUID.randomUUID();
        UUID idDestino = UUID.randomUUID();

        Conta contaOrigem = Conta.builder()
                .id(idOrigem)
                .nome("João")
                .saldo(new BigDecimal("100.00"))
                .build();

        Conta contaDestino = Conta.builder()
                .id(idDestino)
                .nome("Maria")
                .saldo(new BigDecimal("500.00"))
                .build();

        RealizarTransferenciaRequest request = new RealizarTransferenciaRequest(
                idOrigem, idDestino, new BigDecimal("200.00"));

        when(contaRepository.findByIdWithLock(idOrigem)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(idDestino)).thenReturn(Optional.of(contaDestino));

        assertThrows(SaldoInsuficienteException.class, () -> transferenciaService.transferir(request));
        verify(contaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoContaOrigemNaoEncontrada() {
        UUID idOrigem = UUID.randomUUID();
        UUID idDestino = UUID.randomUUID();

        RealizarTransferenciaRequest request = new RealizarTransferenciaRequest(
                idOrigem, idDestino, new BigDecimal("200.00"));

        when(contaRepository.findByIdWithLock(idOrigem)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> transferenciaService.transferir(request));
        verify(contaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoContaDestinoNaoEncontrada() {
        UUID idOrigem = UUID.randomUUID();
        UUID idDestino = UUID.randomUUID();

        Conta contaOrigem = Conta.builder()
                .id(idOrigem)
                .nome("João")
                .saldo(new BigDecimal("1000.00"))
                .build();

        RealizarTransferenciaRequest request = new RealizarTransferenciaRequest(
                idOrigem, idDestino, new BigDecimal("200.00"));

        when(contaRepository.findByIdWithLock(idOrigem)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByIdWithLock(idDestino)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> transferenciaService.transferir(request));
        verify(contaRepository, never()).save(any());
    }


}

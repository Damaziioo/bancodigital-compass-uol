package com.banco.bancodigital.service;

import com.banco.bancodigital.domain.entity.Conta;
import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;
import com.banco.bancodigital.exception.ContaNaoEncontradaException;
import com.banco.bancodigital.repository.ContaRepository;
import com.banco.bancodigital.service.impl.ContaServiceImpl;
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
public class ContaServiceImplTest {
    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaServiceImpl contaService;

    @Test
    void deveCriarContaComSucesso() {
        CriarContaRequest request = new CriarContaRequest("João", new BigDecimal("1000.00"));

        Conta conta =  Conta.builder()
                .id(UUID.randomUUID())
                .nome("João")
                .saldo(new BigDecimal("1000.00"))
                .dataCriacao(LocalDateTime.now())
                .build();
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaResponse response = contaService.criar(request);

        assertNotNull(response);
        assertEquals("João", response.nome());
        assertEquals(new BigDecimal("1000.00"), response.saldo());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void deveLancarExcecaoQuandoSaldoNegativo(){
        CriarContaRequest request = new CriarContaRequest("João", new BigDecimal("-10.00"));

        assertThrows(IllegalArgumentException.class, () -> contaService.criar(request));
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void deveBuscarContaPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        Conta conta = Conta.builder()
                .id(id)
                .nome("Lucas")
                .saldo(new BigDecimal("100.00"))
                .dataCriacao(LocalDateTime.now())
                .build();

        when(contaRepository.findById(any())).thenReturn(Optional.of(conta));

        ContaResponse response = contaService.buscarPorId(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("Lucas", response.nome());
        assertEquals(new BigDecimal("100.00"), response.saldo());
        verify(contaRepository, times(1)).findById(id);

    }

    @Test
    void deveLancarExcecaoQuandoContaNaoEncontrada(){
        UUID id = UUID.randomUUID();

        when(contaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.buscarPorId(id));
        verify(contaRepository, times(1)).findById(id);
    }
}

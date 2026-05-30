package com.banco.bancodigital.service.impl;

import com.banco.bancodigital.domain.entity.Conta;
import com.banco.bancodigital.domain.entity.Transferencia;
import com.banco.bancodigital.dto.request.RealizarTransferenciaRequest;
import com.banco.bancodigital.dto.response.TransferenciaResponse;
import com.banco.bancodigital.exception.ContaNaoEncontradaException;
import com.banco.bancodigital.exception.SaldoInsuficienteException;
import com.banco.bancodigital.exception.TransferenciaMesmaContaException;
import com.banco.bancodigital.repository.ContaRepository;
import com.banco.bancodigital.repository.TransferenciaRepository;
import com.banco.bancodigital.service.NotificacaoService;
import com.banco.bancodigital.service.TransferenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;
    private final NotificacaoService notificacaoService;

    @Override
    @Transactional
    public TransferenciaResponse transferir(RealizarTransferenciaRequest request) {
        if (request.contaOrigemId().equals(request.contaDestinoId()))
            throw new TransferenciaMesmaContaException();

        Conta contaOrigem = contaRepository.findByIdWithLock(request.contaOrigemId())
                .orElseThrow(()-> new ContaNaoEncontradaException(request.contaOrigemId()));

        Conta contaDestino = contaRepository.findByIdWithLock(request.contaDestinoId())
                .orElseThrow(()-> new ContaNaoEncontradaException(request.contaDestinoId()));

        if(contaOrigem.getSaldo().compareTo(request.valor()) < 0)
            throw new SaldoInsuficienteException();

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transferencia transferencia = Transferencia.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .valor(request.valor())
                .build();

        Transferencia transferenciaSalva =  transferenciaRepository.saveAndFlush(transferencia);

        notificacaoService.notificar(transferenciaSalva);

        return TransferenciaResponse.from(transferenciaSalva);
    }

    @Override
    public List<TransferenciaResponse> listarTransferencias(UUID id) {
        return transferenciaRepository
                .findByContaOrigemIdOrContaDestinoIdOrderByDataHoraDesc(id,id)
                .stream()
                .map(TransferenciaResponse::from)
                .toList();
    }
}

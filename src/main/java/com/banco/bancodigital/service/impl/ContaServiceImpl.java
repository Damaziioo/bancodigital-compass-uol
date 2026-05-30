package com.banco.bancodigital.service.impl;

import com.banco.bancodigital.domain.entity.Conta;
import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;
import com.banco.bancodigital.exception.ContaNaoEncontradaException;
import com.banco.bancodigital.repository.ContaRepository;
import com.banco.bancodigital.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;

    @Override
    public ContaResponse criar(CriarContaRequest request) {
        if(request.saldo().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo");

        Conta conta = Conta.builder()
                .nome(request.nome())
                .saldo(request.saldo())
                .build();
        Conta contaSalva = contaRepository.save(conta);

        return ContaResponse.from(contaSalva);
    }

    @Override
    public ContaResponse buscarPorId(UUID id) {
        return ContaResponse.from(buscarConta(id));
    }

    @Override
    public List<ContaResponse> listarTodas() {
        return contaRepository.findAll().stream()
                .map(ContaResponse::from)
                .toList();
    }

    private Conta buscarConta(UUID id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }
}

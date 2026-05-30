package com.banco.bancodigital.dto.response;

import com.banco.bancodigital.domain.entity.Conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContaResponse(
        UUID id,
        String nome,
        BigDecimal saldo,
        LocalDateTime dataCriacao
) {

    public static ContaResponse from(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNome(),
                conta.getSaldo(),
                conta.getDataCriacao()
        );
    }
}

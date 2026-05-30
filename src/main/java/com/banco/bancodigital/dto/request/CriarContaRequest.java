package com.banco.bancodigital.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarContaRequest(
        @NotBlank String nome,
        @NotNull BigDecimal saldo
) {
}

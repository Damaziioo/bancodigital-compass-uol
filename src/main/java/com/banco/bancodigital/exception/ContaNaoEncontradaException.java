package com.banco.bancodigital.exception;

import java.util.UUID;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(UUID id) {
        super("Conta não encontrada com o id: " + id);
    }
}

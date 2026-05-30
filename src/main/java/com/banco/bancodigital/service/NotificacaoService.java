package com.banco.bancodigital.service;

import com.banco.bancodigital.domain.entity.Transferencia;

public interface NotificacaoService {
    void notificar(Transferencia transferencia);
}

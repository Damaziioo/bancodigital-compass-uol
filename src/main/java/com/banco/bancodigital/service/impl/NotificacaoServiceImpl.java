package com.banco.bancodigital.service.impl;

import com.banco.bancodigital.domain.entity.Transferencia;
import com.banco.bancodigital.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificacaoServiceImpl implements NotificacaoService {
    @Override
    @Async
    public void notificar(Transferencia transferencia) {
        try {
            log.info("Notificação enviada para {} | Transferência de R$ {} realizada com sucesso | Data: {}",
                    transferencia.getContaOrigem().getNome(),
                    transferencia.getValor(),
                    transferencia.getDataHora());

            log.info("Notificação enviada para {} | Você recebeu R$ {} | Data: {}",
                    transferencia.getContaDestino().getNome(),
                    transferencia.getValor(),
                    transferencia.getDataHora());
        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e.getMessage());
        }
    }
}

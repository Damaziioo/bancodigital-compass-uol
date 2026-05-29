package com.banco.bancodigital.repository;

import com.banco.bancodigital.domain.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferenciaRepository extends JpaRepository<Transferencia, UUID> {
    List<Transferencia> findByContaOrigemIdOrContaDestinoIdOrderByDataHoraDesc(UUID contaOrigemId, UUID contaDestinoId);
}

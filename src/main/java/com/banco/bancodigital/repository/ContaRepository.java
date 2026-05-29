package com.banco.bancodigital.repository;

import com.banco.bancodigital.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
}

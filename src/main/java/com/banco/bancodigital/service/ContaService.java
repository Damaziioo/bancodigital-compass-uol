package com.banco.bancodigital.service;

import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;

import java.util.List;
import java.util.UUID;

public interface ContaService {
    ContaResponse criar(CriarContaRequest request);
    ContaResponse buscarPorId(UUID id);
    List<ContaResponse> listarTodas();
}

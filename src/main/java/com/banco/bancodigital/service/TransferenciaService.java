package com.banco.bancodigital.service;

import com.banco.bancodigital.dto.request.RealizarTransferenciaRequest;
import com.banco.bancodigital.dto.response.TransferenciaResponse;

import java.util.List;
import java.util.UUID;

public interface TransferenciaService {
    TransferenciaResponse transferir(RealizarTransferenciaRequest request);
    List<TransferenciaResponse> listarTransferencias(UUID id);
}

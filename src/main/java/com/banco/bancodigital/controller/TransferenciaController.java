package com.banco.bancodigital.controller;

import com.banco.bancodigital.dto.request.RealizarTransferenciaRequest;
import com.banco.bancodigital.dto.response.TransferenciaResponse;
import com.banco.bancodigital.service.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService
    ) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<TransferenciaResponse> realizarTransferencia(
            @Valid @RequestBody RealizarTransferenciaRequest realizarTransferenciaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferenciaService.transferir(realizarTransferenciaRequest));
    }

    @GetMapping("/{contaId}")
    public ResponseEntity<List<TransferenciaResponse>> historicoTransferencias(
            @PathVariable UUID contaId
    ) {
        return ResponseEntity.ok().body(transferenciaService.listarTransferencias(contaId));
    }

}

package com.banco.bancodigital.controller;

import com.banco.bancodigital.dto.request.RealizarTransferenciaRequest;
import com.banco.bancodigital.dto.response.TransferenciaResponse;
import com.banco.bancodigital.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/transferencias")
@Tag(name = "Transferências", description = "Operações de transferência entre contas")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService
    ) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    @Operation(summary = "Realizar uma transferência")
    public ResponseEntity<TransferenciaResponse> realizarTransferencia(
            @Valid @RequestBody RealizarTransferenciaRequest realizarTransferenciaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferenciaService.transferir(realizarTransferenciaRequest));
    }

    @GetMapping("/{contaId}")
    @Operation(summary = "Listar histórico de transferências")
    public ResponseEntity<List<TransferenciaResponse>> historicoTransferencias(
            @PathVariable UUID contaId
    ) {
        return ResponseEntity.ok().body(transferenciaService.listarTransferencias(contaId));
    }

}

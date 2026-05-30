package com.banco.bancodigital.controller;

import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;
import com.banco.bancodigital.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/contas")
public class ContasController {

    private final ContaService contaService;

    public ContasController(ContaService contasService) {
        this.contaService = contasService;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criarConta(@Valid @RequestBody CriarContaRequest criarContaRequest) {
        ContaResponse contaResponse = contaService.criar(criarContaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok().body(contaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ContaResponse>> buscarContas() {
        return ResponseEntity.ok().body(contaService.listarTodas());
    }
}

package com.banco.bancodigital.controller;

import com.banco.bancodigital.dto.request.CriarContaRequest;
import com.banco.bancodigital.dto.response.ContaResponse;
import com.banco.bancodigital.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/contas")
@Tag(name="Contas", description = "Gerenciamento de contas")
public class ContasController {

    private final ContaService contaService;

    public ContasController(ContaService contasService) {
        this.contaService = contasService;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova conta")
    public ResponseEntity<ContaResponse> criarConta(@Valid @RequestBody CriarContaRequest criarContaRequest) {
        ContaResponse contaResponse = contaService.criar(criarContaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok().body(contaService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as contas")
    public ResponseEntity<List<ContaResponse>> buscarContas() {
        return ResponseEntity.ok().body(contaService.listarTodas());
    }
}

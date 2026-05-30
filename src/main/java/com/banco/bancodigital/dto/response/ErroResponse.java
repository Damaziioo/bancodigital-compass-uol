package com.banco.bancodigital.dto.response;

import java.time.LocalDateTime;

public record ErroResponse(int status, String mensagem, LocalDateTime dataHora) {
}

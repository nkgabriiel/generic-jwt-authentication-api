package com.springboot.comercio.dto.response;

public record ClienteResponseDTO (
        Long id,
        String nome,
        String cpf,
        String email,
        Integer pontoFidelidade
) {
}

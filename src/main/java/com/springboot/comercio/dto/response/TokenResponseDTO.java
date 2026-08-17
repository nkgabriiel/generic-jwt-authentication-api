package com.springboot.comercio.dto.response;

public record TokenResponseDTO(
        String token,
        String tipo,
        String email,
        String role,
        String refreshToken
) {
}

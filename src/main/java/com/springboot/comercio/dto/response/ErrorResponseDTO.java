package com.springboot.comercio.dto.response;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponseDTO(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ErrorResponseDTO of(HttpStatus status, String message, String path) {
        return new ErrorResponseDTO(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }
}

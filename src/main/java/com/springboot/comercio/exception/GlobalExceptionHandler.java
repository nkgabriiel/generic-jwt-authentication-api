package com.springboot.comercio.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ClienteNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 404,
                "erro", "Recurso não encontrado",
                "mensagem", ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 400,
                "erro", "Erro de validação",
                "mensagem", "Um ou mais campos são inválidos",
                "detalhes", detalhes,
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request) {


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 500,
                "erro", "Erro interno",
                "mensagem", "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                "path", request.getRequestURI()
        ));
    }
}


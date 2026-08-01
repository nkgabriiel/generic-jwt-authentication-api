package com.springboot.comercio.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado com o ID: " + id);
    }
}

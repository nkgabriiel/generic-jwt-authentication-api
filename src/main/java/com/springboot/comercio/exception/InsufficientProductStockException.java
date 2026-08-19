package com.springboot.comercio.exception;

public class InsufficientProductStockException extends RuntimeException {
    public InsufficientProductStockException(String message) {
        super(message);
    }
}

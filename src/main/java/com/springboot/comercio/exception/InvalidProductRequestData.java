package com.springboot.comercio.exception;

public class InvalidProductRequestData extends RuntimeException {
    public InvalidProductRequestData(String message) {
        super(message);
    }
}

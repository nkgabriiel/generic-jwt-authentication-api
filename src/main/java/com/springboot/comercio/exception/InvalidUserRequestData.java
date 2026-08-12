package com.springboot.comercio.exception;

public class InvalidUserRequestData extends RuntimeException {
    public InvalidUserRequestData(String message) {
        super(message);
    }
}

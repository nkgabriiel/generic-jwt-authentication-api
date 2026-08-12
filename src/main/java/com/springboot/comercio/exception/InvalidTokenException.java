package com.springboot.comercio.exception;


import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

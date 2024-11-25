package com.web.aldalu.aldalu.exceptions.dtos;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}

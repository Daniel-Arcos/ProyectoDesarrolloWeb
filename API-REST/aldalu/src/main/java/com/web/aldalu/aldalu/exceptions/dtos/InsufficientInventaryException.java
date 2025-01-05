package com.web.aldalu.aldalu.exceptions.dtos;

import lombok.Data;

@Data
public class InsufficientInventaryException extends RuntimeException{
    private final String message;

    public InsufficientInventaryException(String message) {
        super(message);
        this.message = message;
    }
}

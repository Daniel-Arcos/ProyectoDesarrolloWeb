package com.web.aldalu.aldalu.exceptions.dtos;

import lombok.Data;

@Data
public class DuplicatedKeyException extends RuntimeException{
    
    private final String message;

    public DuplicatedKeyException(String message) {
        super(message);
        this.message = message;
    }
}

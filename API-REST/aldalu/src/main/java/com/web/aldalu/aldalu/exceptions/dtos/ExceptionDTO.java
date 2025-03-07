package com.web.aldalu.aldalu.exceptions.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDTO {
     private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ExceptionDTO() {
        this.timestamp = LocalDateTime.now();
    }
}

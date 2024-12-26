package com.web.aldalu.aldalu.exceptions.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AppException  extends RuntimeException{
    
    private final String code;
    private final int responseCode;
    private final List<ExceptionDTO> errorList = new ArrayList<>();

    public AppException(String code, int responseCode, String message) {
        super(message);
        this.code = code;
        this.responseCode = responseCode;
    }
}

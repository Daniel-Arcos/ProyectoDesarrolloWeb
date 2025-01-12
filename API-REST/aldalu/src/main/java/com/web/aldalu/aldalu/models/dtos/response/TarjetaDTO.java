package com.web.aldalu.aldalu.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TarjetaDTO {
    private Long id;
    private String numeroTarjeta;
    private String nombreEmisor;
    private String anoVencimiento;
    private String mesVencimiento;
    private String emisorTarjeta;
    private String nombreTitular;
}

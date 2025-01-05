package com.web.aldalu.aldalu.models.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.EmisorTarjeta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaRequestDTO {
    private String anoVencimiento;
    private String mesVencimiento;
    private int codigoSeguridad;
    private EmisorTarjeta emisorTarjeta;
    private String nombreTitular;
    private String numeroTarjeta;
}

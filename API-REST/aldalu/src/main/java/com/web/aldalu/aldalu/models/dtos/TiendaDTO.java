package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TiendaDTO {
    private Long idVendedor;
    private String descripcion;
    private String nombreTienda;
}

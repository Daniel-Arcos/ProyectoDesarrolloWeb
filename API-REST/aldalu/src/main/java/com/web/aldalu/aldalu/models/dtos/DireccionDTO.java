package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DireccionDTO {
    private String calle;
    private String ciudad;
    private int codigoPostal;
    private String colonia;
    private String entidadFederativa;
    private String municipio;
    private String numeroExterior;
    private String numeroInterior;
    private CoordenadasDireccionDTO coordenadasDireccion;
}

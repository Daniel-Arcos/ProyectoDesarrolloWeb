package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResenaDTO {
    private Long id;
    private String comentario;
    private double calificacion;
    private Long clienteId;
}

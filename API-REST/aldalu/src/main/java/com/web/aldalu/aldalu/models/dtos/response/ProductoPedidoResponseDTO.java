package com.web.aldalu.aldalu.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.Categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoResponseDTO {
    private Long idProducto;
    private float cantidad;
    private float precioUnitario;
    private String nombre;
    private String descripcion;
    private Categoria categoria;
    private byte[] imageData;
}

package com.web.aldalu.aldalu.models.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoDTO {
    private Long idProducto;
    private Long idTienda;
    private Float cantidad;
    private Float precioUnitario;
}

package com.web.aldalu.aldalu.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.EstadoPedidoTienda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoTiendaAdminDTO {
    private Long id;
    private EstadoPedidoTienda estadoPedidoTienda;
    private double subtotal;
    private int cantidadProductos;
    private String nombreTienda;
}

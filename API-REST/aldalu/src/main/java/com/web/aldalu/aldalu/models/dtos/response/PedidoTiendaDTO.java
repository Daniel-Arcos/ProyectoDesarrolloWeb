package com.web.aldalu.aldalu.models.dtos.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.entities.InformacionPago;
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
public class PedidoTiendaDTO {
    private Long id;
    private String nombreCliente;
    private InformacionPago informacionPago;
    private EstadoPedidoTienda estadoPedidoTienda;
    private LocalDateTime fechaCreacion;
    private double total;
}

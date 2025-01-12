package com.web.aldalu.aldalu.models.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.entities.InformacionEnvio;
import com.web.aldalu.aldalu.models.entities.InformacionPago;
import com.web.aldalu.aldalu.models.enums.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseAdminDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private EstadoPedido estadoPedido;
    private Float total;
    private String nombreCliente;
    private InformacionEnvio informacionEnvio;
    private InformacionPago informacionPago;
    private int cantidadProductos;
    private List<PedidoTiendaAdminDTO> pedidosTienda;
    private Long idRepartidor;
}

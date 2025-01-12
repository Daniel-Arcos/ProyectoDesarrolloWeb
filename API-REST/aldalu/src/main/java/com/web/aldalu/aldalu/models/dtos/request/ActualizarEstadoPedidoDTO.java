package com.web.aldalu.aldalu.models.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ActualizarEstadoPedidoDTO {
    private EstadoPedido nuevoEstado;
    private Long idRepartidor;
}

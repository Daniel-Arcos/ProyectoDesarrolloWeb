package com.web.aldalu.aldalu.models.dtos.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.dtos.ProductoPedidoDTO;
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
public class PedidoRequestDTO {
    
    private LocalDateTime fechaCreacion;
    private EstadoPedido estadoPedido;
    private Float total;
    private Long idCliente;
    private Long idDireccion;
    private Long idTarjeta;
    private List<ProductoPedidoDTO> productos;
}

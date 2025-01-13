package com.web.aldalu.aldalu.services;

import java.util.List;

import com.web.aldalu.aldalu.models.dtos.request.ActualizarEstadoPedidoDTO;
import com.web.aldalu.aldalu.models.dtos.request.PedidoRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseAdminDTO;
import com.web.aldalu.aldalu.models.dtos.response.ProductoPedidoResponseDTO;
import com.web.aldalu.aldalu.models.enums.EstadoPedido;
import com.web.aldalu.aldalu.models.enums.EstadoPedidoTienda;

public interface IPedidoService {
    List<PedidoResponseAdminDTO> obtenerPedidos();
    PedidoRequestDTO guardarPedido(PedidoRequestDTO pedido);
    void actualizarEstadoPedidotienda(Long id, EstadoPedidoTienda estadoPedidoTienda);
    void asignarRepartidorParaEnvio(Long id, ActualizarEstadoPedidoDTO estadoPedido);
    List<ProductoPedidoResponseDTO> obtenerProductosPedido(Long id);
    List<ProductoPedidoResponseDTO> obtenerProductosPedidoTienda(Long id);
    PedidoResponseAdminDTO obtenerPedidoPorId(Long id);
    void atualizarEstadoPedido(Long id, EstadoPedido nuevoEstado);
}

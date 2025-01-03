package com.web.aldalu.aldalu.services;

import java.util.List;

import com.web.aldalu.aldalu.models.dtos.request.PedidoRequestDTO;

import jakarta.validation.Valid;

public interface IPedidoService {
    List<PedidoRequestDTO> obtenerPedidos();
    PedidoRequestDTO guardarPedido(PedidoRequestDTO pedido);
}

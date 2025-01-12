package com.web.aldalu.aldalu.services;

import java.util.List;

import com.web.aldalu.aldalu.models.dtos.DireccionDTO;
import com.web.aldalu.aldalu.models.dtos.request.ClienteDTO;
import com.web.aldalu.aldalu.models.dtos.request.TarjetaRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.TarjetaDTO;

public interface IClienteService {
    List<TarjetaDTO> obtenerTarjetasCliente(Long idCliente);
    TarjetaDTO guardarTarjetaCliente(Long idCliente, TarjetaRequestDTO tarjetaDTO);
    List<DireccionDTO> obtenerDireccionesCliente(Long idCliente);
    DireccionDTO guardarDireccionCliente(Long idCliente, DireccionDTO direccion);
    DireccionDTO actualizarDirecccion(Long id, DireccionDTO productoRequestDTO);
    void eliminarDireccion(Long idDireccion);
    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);
    void eliminarTarjeta(Long idTarjeta);
    List<PedidoResponseDTO> obtenerPedidosCliente(Long idCliente);
}

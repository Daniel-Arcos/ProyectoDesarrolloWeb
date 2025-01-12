package com.web.aldalu.aldalu.services;

import java.util.List;

import com.web.aldalu.aldalu.models.dtos.request.RepartidorRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.EmpleadoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseRepartidorDTO;
import com.web.aldalu.aldalu.models.dtos.response.RepartidorResponseDTO;

public interface IEmpleadoService {

    List<RepartidorResponseDTO> obtenerRepartidores();
    RepartidorResponseDTO guardarRepartidor(RepartidorRequestDTO repartidor);
    List<PedidoResponseRepartidorDTO> obtenerPedidosRepartidor(Long id);
    EmpleadoResponseDTO obtenerEmpleadoPorId(Long id);
    
}

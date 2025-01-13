package com.web.aldalu.aldalu.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.RepartidorRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.EmpleadoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseRepartidorDTO;
import com.web.aldalu.aldalu.models.dtos.response.RepartidorResponseDTO;
import com.web.aldalu.aldalu.services.IEmpleadoService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_EMPLEADOS)
@RequiredArgsConstructor
public class EmpleadoController {
    
    private final IEmpleadoService empleadoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorId(@NonNull @PathVariable final Long id) {
        try {
            EmpleadoResponseDTO empleadoResponseDTO = empleadoService.obtenerEmpleadoPorId(id);
            return ResponseEntity.ok(empleadoResponseDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/repartidores")
    public ResponseEntity<List<RepartidorResponseDTO>> obtenerRepartidores() {
        List<RepartidorResponseDTO> repartidores = empleadoService.obtenerRepartidores();
        return ResponseEntity.ok(repartidores); 
    }

    @GetMapping(value = "/repartidores/{id}/pedidos")
    public ResponseEntity<List<PedidoResponseRepartidorDTO>> obtenerPedidosRepartidor(@NonNull @PathVariable final Long id) {
        try {
            List<PedidoResponseRepartidorDTO> pedidos = empleadoService.obtenerPedidosRepartidor(id);
            return ResponseEntity.ok(pedidos); 
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/repartidores")
    public ResponseEntity<RepartidorResponseDTO> guardarRepartidor(@Valid @RequestBody final RepartidorRequestDTO repartidor) {
        try {
            RepartidorResponseDTO repartidorGuardado = empleadoService.guardarRepartidor(repartidor);
            return ResponseEntity.status(HttpStatus.CREATED).body(repartidorGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

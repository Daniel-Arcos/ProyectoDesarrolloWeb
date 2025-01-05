package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.DireccionDTO;
import com.web.aldalu.aldalu.models.dtos.request.TarjetaRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.TarjetaDTO;
import com.web.aldalu.aldalu.models.entities.Direccion;
import com.web.aldalu.aldalu.models.entities.Tarjeta;
import com.web.aldalu.aldalu.services.IClienteService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_CLIENTES)
@RequiredArgsConstructor
public class ClienteController {
    
    private final IClienteService clienteServiceImpl;

    @GetMapping(value = "/{idCliente}/tarjetas")
    public ResponseEntity<List<TarjetaDTO>> obtenerTarjetasCliente (@NonNull @PathVariable final Long idCliente) {
        try {
            List<TarjetaDTO> tarjetas = clienteServiceImpl.obtenerTarjetasCliente(idCliente);
            return ResponseEntity.ok(tarjetas);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/{idCliente}/tarjetas")
    public ResponseEntity<TarjetaDTO> guardarTarjetaCliente (@NonNull @PathVariable final Long idCliente, @NonNull @Valid @RequestBody final TarjetaRequestDTO tarjetaRequestDTO) {
        try {
            TarjetaDTO tarjetaGuardada = clienteServiceImpl.guardarTarjetaCliente(idCliente, tarjetaRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaGuardada);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{idCliente}/direcciones")
    public ResponseEntity<List<DireccionDTO>> obtenerDireccionesCliente (@NonNull @PathVariable final Long idCliente) {
        try {
            List<DireccionDTO> direcciones = clienteServiceImpl.obtenerDireccionesCliente(idCliente);
            return ResponseEntity.ok(direcciones);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/{idCliente}/direcciones")
    public ResponseEntity<DireccionDTO> guardarDireccionCliente (@NonNull @PathVariable final Long idCliente, @NonNull @Valid @RequestBody final DireccionDTO direccion) {
        try {
            DireccionDTO direccionGuardada = clienteServiceImpl.guardarDireccionCliente(idCliente, direccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(direccionGuardada);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // @PutMapping(value = "/{id}/tarjetas/{idTarjeta}")
    // public List<Tarjeta> actualizarTarjeta (@NonNull @PathVariable final Long id) {
        
    // }

    // @DeleteMapping(value = "/{id}/tarjetas/{idTarjeta}")
    // public List<Tarjeta> eliminarTarjeta (@NonNull @PathVariable final Long id) {
        
    // }
}

package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.DireccionDTO;
import com.web.aldalu.aldalu.models.dtos.request.ClienteDTO;
import com.web.aldalu.aldalu.models.dtos.request.TarjetaRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.TarjetaDTO;
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


@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_CLIENTES)
@RequiredArgsConstructor
public class ClienteController {
    
    private final IClienteService clienteServiceImpl;

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final ClienteDTO clienteDTO) {
        try {
            ClienteDTO clienteActualizado = clienteServiceImpl.actualizarCliente(id, clienteDTO);
            return ResponseEntity.ok(clienteActualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

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

    @DeleteMapping(value = "/tarjetas/{idTarjeta}")
    public ResponseEntity<?> eliminarTarjeta(@NonNull @PathVariable final Long idTarjeta) {
        try {
            clienteServiceImpl.eliminarTarjeta(idTarjeta);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
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

    @PutMapping(value = "/direcciones/{id}")
    public ResponseEntity<DireccionDTO> actualizarDireccion(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final DireccionDTO direccionDTO) {
        try {
            DireccionDTO direccionActualizada = clienteServiceImpl.actualizarDirecccion(id, direccionDTO);
            return ResponseEntity.ok(direccionActualizada);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/direcciones/{idDireccion}")
    public ResponseEntity<?> eliminarDirecccion(@NonNull @PathVariable final Long idDireccion) {
        try {
            clienteServiceImpl.eliminarDireccion(idDireccion);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @GetMapping(value = "/{idCliente}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosCliente (@NonNull @PathVariable final Long idCliente) {
        try {
            List<PedidoResponseDTO> pedidos = clienteServiceImpl.obtenerPedidosCliente(idCliente);
            return ResponseEntity.ok(pedidos);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}

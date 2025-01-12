package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.ActualizarEstadoPedidoDTO;
import com.web.aldalu.aldalu.models.dtos.request.ActualizarEstadoPedidoTiendaDTO;
import com.web.aldalu.aldalu.models.dtos.request.PedidoRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseAdminDTO;
import com.web.aldalu.aldalu.models.dtos.response.ProductoPedidoResponseDTO;
import com.web.aldalu.aldalu.services.IPedidoService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_PEDIDOS)
@RequiredArgsConstructor
public class PedidosController {
    
    private final IPedidoService pedidoServiceImpl;

    @GetMapping()
    public ResponseEntity<List<PedidoResponseAdminDTO>> obtenerPedidos() {
        List<PedidoResponseAdminDTO> pedidos = pedidoServiceImpl.obtenerPedidos();
        return ResponseEntity.ok(pedidos); 
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PedidoResponseAdminDTO> obtenerPedidoPorID(@NonNull @PathVariable final Long id) {
        try {
            PedidoResponseAdminDTO pedido = pedidoServiceImpl.obtenerPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping()
    public ResponseEntity<PedidoRequestDTO> guardarPedido(@Valid @RequestBody final PedidoRequestDTO pedido) {
        try {
            PedidoRequestDTO pedidoGuardado = pedidoServiceImpl.guardarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/pedidosTienda/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedidoTienda(@NonNull @PathVariable final Long id, @Valid @RequestBody final ActualizarEstadoPedidoTiendaDTO estadoPedidoTienda) {
        try {
            System.out.println(estadoPedidoTienda);
            pedidoServiceImpl.actualizarEstadoPedidotienda(id, estadoPedidoTienda.getNuevoEstado());
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}/preparar-envio")
    public ResponseEntity<?> asignarRepartidor(@NonNull @PathVariable final Long id, @Valid @RequestBody final ActualizarEstadoPedidoDTO estadoPedido) {
        try {
            pedidoServiceImpl.asignarRepartidorParaEnvio(id, estadoPedido);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@NonNull @PathVariable final Long id, @Valid @RequestBody final ActualizarEstadoPedidoDTO estadoPedido) {
        try {
            pedidoServiceImpl.atualizarEstadoPedido(id, estadoPedido.getNuevoEstado());
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/productos")
    public ResponseEntity<List<ProductoPedidoResponseDTO>> obtenerProductosPedido(@NonNull @PathVariable final Long id) {
        try {
            List<ProductoPedidoResponseDTO> productos = pedidoServiceImpl.obtenerProductosPedido(id);
            return ResponseEntity.ok(productos);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/pedidosTienda/{id}/productos")
    public ResponseEntity<List<ProductoPedidoResponseDTO>> obtenerProductosPedidoTienda(@NonNull @PathVariable final Long id) {
        try {
            List<ProductoPedidoResponseDTO> productos = pedidoServiceImpl.obtenerProductosPedidoTienda(id);
            return ResponseEntity.ok(productos);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    

}

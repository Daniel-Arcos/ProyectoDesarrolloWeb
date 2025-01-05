package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.PedidoRequestDTO;
import com.web.aldalu.aldalu.models.entities.Pedido;
import com.web.aldalu.aldalu.services.IPedidoService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_PEDIDOS)
@RequiredArgsConstructor
public class PedidosController {
    
    private final IPedidoService pedidoServiceImpl;

    @GetMapping()
    public ResponseEntity<List<PedidoRequestDTO>> obtenerPedidos() {
        List<PedidoRequestDTO> pedidos = pedidoServiceImpl.obtenerPedidos();
        return ResponseEntity.ok(pedidos); 
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
    

}

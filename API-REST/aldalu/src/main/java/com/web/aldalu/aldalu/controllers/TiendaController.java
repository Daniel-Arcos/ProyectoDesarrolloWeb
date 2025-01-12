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
import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoTiendaDTO;
import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.services.ITiendaService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_TIENDAS)
@RequiredArgsConstructor
public class TiendaController {
    
    private final ITiendaService tiendaServiceImpl;

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTiendas() {
        List<Tienda> tiendas = tiendaServiceImpl.obtenerTiendas();
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping(value = "/{id}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosPorTienda(@NonNull @PathVariable final Long id) {
        try {
            List<Producto> productos = tiendaServiceImpl.obtenerProductosPorTienda(id);
            return ResponseEntity.ok(productos);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}/pedidos")
    public ResponseEntity<List<PedidoTiendaDTO>> obtenerPedidosPorTienda(@NonNull @PathVariable final Long id) {
        try {
            List<PedidoTiendaDTO> pedidosTienda = tiendaServiceImpl.obtenerPedidosPorTienda(id);
            return ResponseEntity.ok(pedidosTienda);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Tienda> guardarTienda(@NonNull @Valid @RequestBody final TiendaDTO tienda) {
        try {
            Tienda tiendaGuardado = tiendaServiceImpl.guardarTienda(tienda);
            return ResponseEntity.status(HttpStatus.CREATED).body(tiendaGuardado);
        } 
        catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

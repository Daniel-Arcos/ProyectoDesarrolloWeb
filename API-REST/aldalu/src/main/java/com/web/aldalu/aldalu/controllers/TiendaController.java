package com.web.aldalu.aldalu.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.services.impl.TiendaServiceImpl;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/tiendas")
@RequiredArgsConstructor
public class TiendaController {
    
    private final TiendaServiceImpl tiendaServiceImpl;

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTiendas() {
        List<Tienda> tiendas = tiendaServiceImpl.obtenerTiendas();
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Tienda> obtenerTiendaPorId(@NonNull @PathVariable final Long id) {
        Optional<Tienda> tiendaOptional = tiendaServiceImpl.obtenerTiendaPorId(id);
        if(tiendaOptional.isPresent()) {
            return ResponseEntity.ok(tiendaOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosPorTienda(@NonNull @PathVariable final Long id) {
        List<Producto> productos = tiendaServiceImpl.obtenerProductosPorTienda(id);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Tienda> guardarTienda(@NonNull @Valid @RequestBody final Tienda tienda) {
        try {
            Tienda tiendaGuardado = tiendaServiceImpl.guardarTienda(tienda);
            return ResponseEntity.status(HttpStatus.CREATED).body(tiendaGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> eliminarTienda(@NonNull @PathVariable final Long id) {
        Tienda tienda = new Tienda();
        tienda.setId(id);
        Optional<Tienda> tiendaOptional = tiendaServiceImpl.eliminarTienda(tienda);
        if (tiendaOptional.isPresent()) {
            return ResponseEntity.ok(tiendaOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}

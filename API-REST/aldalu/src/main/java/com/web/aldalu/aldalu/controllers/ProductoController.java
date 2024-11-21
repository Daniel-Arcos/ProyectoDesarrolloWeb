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
import com.web.aldalu.aldalu.services.impl.ProductoServiceImpl;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServiceImpl productoServiceImpl;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerpProductos() {
        List<Producto> productos = productoServiceImpl.obtenerProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Producto> obtenerproductoPorId(@NonNull @PathVariable final Long id) {
        Optional<Producto> productoOptional = productoServiceImpl.obtenerProductoPorId(id);
        if(productoOptional.isPresent()) {
            return ResponseEntity.ok(productoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Producto> guardarproducto(@NonNull @Valid @RequestBody final Producto producto) {
        try {
            Producto productoGuardado = productoServiceImpl.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> eliminarproducto(@NonNull @PathVariable final Long id) {
        Producto producto = new Producto();
        producto.setId(id);
        Optional<Producto> productoOptional = productoServiceImpl.eliminarProducto(producto);
        if (productoOptional.isPresent()) {
            return ResponseEntity.ok(productoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }   
}

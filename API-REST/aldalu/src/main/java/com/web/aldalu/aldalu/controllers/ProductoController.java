package com.web.aldalu.aldalu.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.ProductoRequestDTO;
import com.web.aldalu.aldalu.services.IProductoService;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_PRODUCTOS)
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoServiceImpl;

    @GetMapping
    public ResponseEntity<List<ProductoRequestDTO>> obtenerProductos() {
        List<ProductoRequestDTO> productos = productoServiceImpl.obtenerProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductoRequestDTO> obtenerproductoPorId(@NonNull @PathVariable final Long id) {
        try {
            ProductoRequestDTO productoRequestDTO = productoServiceImpl.obtenerProductoPorId(id);
            return ResponseEntity.ok(productoRequestDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductoRequestDTO> guardarproducto(@NonNull @Valid @RequestBody final ProductoRequestDTO producto) {
        try {
            ProductoRequestDTO productoGuardado = productoServiceImpl.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        }
        catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
   
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductoRequestDTO> actualizarProducto(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final ProductoRequestDTO productoRequest) {
        try {
            ProductoRequestDTO productoActualizado = productoServiceImpl.actualizarProducto(id, productoRequest);
            return ResponseEntity.ok(productoActualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> eliminarproducto(@NonNull @PathVariable final Long id) {
        try {
            productoServiceImpl.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   
}

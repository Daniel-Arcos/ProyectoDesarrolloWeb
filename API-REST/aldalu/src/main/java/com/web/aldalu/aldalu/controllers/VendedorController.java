package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.services.impl.VendedorServiceImpl;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/vendedores")
@RequiredArgsConstructor
public class VendedorController {
    
    private final VendedorServiceImpl vendedorServiceImpl;

    @GetMapping
    public ResponseEntity<List<Vendedor>> obtenerVendedores() {
        List<Vendedor> vendedores = vendedorServiceImpl.obtenerVendedores();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Vendedor> obtenerVendedorPorId(@NonNull @PathVariable final Long id) {
        Optional<Vendedor> vendedorOptional = vendedorServiceImpl.obtenerVendedorPorId(id);
        if(vendedorOptional.isPresent()) {
            return ResponseEntity.ok(vendedorOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Vendedor> guardarVendedor(@NonNull @Valid @RequestBody final Vendedor vendedor) {
        try {
            Vendedor vendedorGuardado = vendedorServiceImpl.guardarVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendedorGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> eliminarVendedor(@NonNull @PathVariable final Long id) {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(id);
        Optional<Vendedor> vendedorOptional = vendedorServiceImpl.eliminarVendedor(vendedor);
        if (vendedorOptional.isPresent()) {
            return ResponseEntity.ok(vendedorOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}

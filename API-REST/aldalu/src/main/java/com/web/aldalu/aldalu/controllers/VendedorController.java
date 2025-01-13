package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.dtos.VendedorDTO;
import com.web.aldalu.aldalu.services.impl.VendedorServiceImpl;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

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
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_VENDEDORES)
@RequiredArgsConstructor
public class VendedorController {
    
    private final VendedorServiceImpl vendedorServiceImpl;

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> obtenerVendedores() {
        List<VendedorDTO> vendedores = vendedorServiceImpl.obtenerVendedores();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VendedorDTO> obtenerVendedorPorId(@NonNull @PathVariable final Long id) {
        Optional<VendedorDTO> vendedorOptional = vendedorServiceImpl.obtenerVendedorPorId(id);
        if(vendedorOptional.isPresent()) {
            return ResponseEntity.ok(vendedorOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}/tienda")
    public ResponseEntity<TiendaDTO> obtenerTiendaVendedor(@NonNull @PathVariable final Long id) {
        try {
            TiendaDTO tienda = vendedorServiceImpl.obtenerTiendaVendedor(id);
            return ResponseEntity.ok(tienda);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    

    @PostMapping
    public ResponseEntity<VendedorDTO> guardarVendedor(@NonNull @Valid @RequestBody final VendedorDTO vendedor) {
        try {
            VendedorDTO vendedorGuardado = vendedorServiceImpl.guardarVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendedorGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VendedorDTO> actualizarVendedor(@NonNull @PathVariable final Long id, @NonNull @Valid @RequestBody final VendedorDTO vendedorDTO) {
        Optional<VendedorDTO> vendedorActualizado = vendedorServiceImpl.actualizarVendedor(id, vendedorDTO);
        if (vendedorActualizado.isPresent()) {
            return ResponseEntity.ok(vendedorActualizado.get());
        }
        return ResponseEntity.notFound().build();
    }

   @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> eliminarVendedor(@NonNull @PathVariable final Long id) {
        Optional<VendedorDTO> vendedorOptional = vendedorServiceImpl.eliminarVendedor(id);
        if (vendedorOptional.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

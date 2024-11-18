package com.web.aldalu.aldalu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.models.Vendedor;
import com.web.aldalu.aldalu.services.VendedorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/vendedores")
public class VendedorController {
    
    @Autowired
    private VendedorService vendedorService;

    @PostMapping
    public ResponseEntity<Vendedor> crearVendedor(@RequestBody Vendedor vendedor) {
        Vendedor nuevoVendedor = vendedorService.guardaVendedor(vendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
    }
    
}

package com.web.aldalu.aldalu.services;

import java.util.List;
import java.util.Optional;

import com.web.aldalu.aldalu.models.entities.Vendedor;

public interface IVendedorService {
    List<Vendedor> obtenerVendedores();
    Optional<Vendedor> obtenerVendedorPorId(Long id);
    Vendedor guardarVendedor(Vendedor vendedor);
    Optional<Vendedor> eliminarVendedor(Vendedor vendedor);
}

package com.web.aldalu.aldalu.services;

import java.util.List;
import java.util.Optional;

import com.web.aldalu.aldalu.models.dtos.VendedorDTO;
import com.web.aldalu.aldalu.models.entities.Vendedor;

import lombok.NonNull;

public interface IVendedorService {
    List<VendedorDTO> obtenerVendedores();
    Optional<VendedorDTO> obtenerVendedorPorId(Long id);
    VendedorDTO guardarVendedor(VendedorDTO vendedor);
    Optional<VendedorDTO> eliminarVendedor(@NonNull Long vendedorId);
    public Optional<VendedorDTO> actualizarVendedor(@NonNull final Long vendedorId, @NonNull final VendedorDTO vendedorDTO);
}

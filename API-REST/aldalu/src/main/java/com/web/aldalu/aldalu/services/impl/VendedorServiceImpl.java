package com.web.aldalu.aldalu.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.repositories.IUsuarioRepository;
import com.web.aldalu.aldalu.repositories.IVendedorRepository;
import com.web.aldalu.aldalu.services.IVendedorService;

import jakarta.validation.Valid;
import lombok.NonNull;

@Service
public class VendedorServiceImpl implements IVendedorService {
    
    @Autowired
    private IVendedorRepository vendedorRepository;

    @Transactional(readOnly = true)
    public List<Vendedor> obtenerVendedores() {
        return vendedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Vendedor> obtenerVendedorPorId(Long id) {
        return vendedorRepository.findById(id);
    }

    @Transactional
    @Override
    public Vendedor guardarVendedor(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    @Override
    public Optional<Vendedor> eliminarVendedor(Vendedor vendedor) {
        Optional<Vendedor> vendedorOptional = vendedorRepository.findById(vendedor.getId());
        vendedorOptional.ifPresent(productDb -> {
            vendedorRepository.delete(productDb);
        });
        return vendedorOptional;
    }

}

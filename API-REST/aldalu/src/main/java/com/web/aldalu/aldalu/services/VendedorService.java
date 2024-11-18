package com.web.aldalu.aldalu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.aldalu.aldalu.models.Vendedor;
import com.web.aldalu.aldalu.repositories.UsuarioRepository;

@Service
public class VendedorService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Vendedor guardaVendedor(Vendedor vendedor) {
        return (Vendedor) usuarioRepository.save(vendedor);
    }


}

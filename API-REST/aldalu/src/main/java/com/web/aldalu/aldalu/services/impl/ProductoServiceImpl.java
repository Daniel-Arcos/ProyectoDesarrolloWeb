package com.web.aldalu.aldalu.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.repositories.IProductoRepository;
import com.web.aldalu.aldalu.services.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> eliminarProducto(Producto producto) {
        Optional<Producto> productoOptional = productoRepository.findById(producto.getId());
        productoOptional.ifPresent(productoDb -> {
            productoRepository.delete(producto);
        });
        return productoOptional;
    }
    
}

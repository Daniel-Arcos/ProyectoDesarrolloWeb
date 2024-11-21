package com.web.aldalu.aldalu.services;

import java.util.List;
import java.util.Optional;

import com.web.aldalu.aldalu.models.entities.Producto;

public interface IProductoService {
    List<Producto> obtenerProductos();
    Optional<Producto> obtenerProductoPorId(Long id);
    Producto guardarProducto(Producto producto);
    Optional<Producto> eliminarProducto(Producto producto);
}

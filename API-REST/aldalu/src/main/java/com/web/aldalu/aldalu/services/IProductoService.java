package com.web.aldalu.aldalu.services;

import java.util.List;
import java.util.Optional;

import com.web.aldalu.aldalu.models.dtos.request.ProductoRequestDTO;
import com.web.aldalu.aldalu.models.entities.Producto;

public interface IProductoService {
    List<ProductoRequestDTO> obtenerProductos();
    ProductoRequestDTO obtenerProductoPorId(Long id);
    ProductoRequestDTO guardarProducto(ProductoRequestDTO producto);
    ProductoRequestDTO actualizarProducto(Long id, ProductoRequestDTO productoRequestDTO);
    void eliminarProducto(Long id);
}

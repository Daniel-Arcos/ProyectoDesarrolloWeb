package com.web.aldalu.aldalu.services;

import java.util.List;
import com.web.aldalu.aldalu.models.dtos.request.ProductoRequestDTO;

public interface IProductoService {
    List<ProductoRequestDTO> obtenerProductos();
    ProductoRequestDTO obtenerProductoPorId(Long id);
    ProductoRequestDTO guardarProducto(ProductoRequestDTO producto);
    ProductoRequestDTO actualizarProducto(Long id, ProductoRequestDTO productoRequestDTO);
    void eliminarProducto(Long id);
}

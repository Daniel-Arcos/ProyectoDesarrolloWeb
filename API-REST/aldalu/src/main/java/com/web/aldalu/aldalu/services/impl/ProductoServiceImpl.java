package com.web.aldalu.aldalu.services.impl;

import java.util.Base64;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.InternalServerErrorException;
import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.ProductoRequestDTO;
import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.repositories.IProductoRepository;
import com.web.aldalu.aldalu.repositories.ITiendaRepository;
import com.web.aldalu.aldalu.services.IProductoService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final IProductoRepository productoRepository;
    private final ITiendaRepository tiendaRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<ProductoRequestDTO> obtenerProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(this::convertToDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoRequestDTO obtenerProductoPorId(Long id) {
        Producto producto = obtenerProductoPorIdHeper(id);
        return convertToDTO(producto);
    }

    @Transactional
    @Override
    public ProductoRequestDTO guardarProducto(ProductoRequestDTO productoDTO) {
        Tienda tienda = tiendaRepository.findById(productoDTO.getTiendaId())
            .orElseThrow(()-> new NotFoundException(ExceptionMessageConstants.TIENDA_NOT_FOUND));
        Producto producto = new Producto();
        producto.setCategoria(productoDTO.getCategoria());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecioVenta(productoDTO.getPrecioVenta());
        producto.setInventario(productoDTO.getInventario());
        producto.setTienda(tienda);
        if (productoDTO.getImageData() != null) {
            producto.setImageData(Base64.getDecoder().decode(productoDTO.getImageData()));
        }
        return convertToDTO(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorIdHeper(id);
        productoRepository.delete(producto);
    }

    @Override
    public ProductoRequestDTO actualizarProducto(Long id, ProductoRequestDTO productoRequestDTO) {
        Producto productoExistente = obtenerProductoPorIdHeper(id);
        actualizarProductoExistente(productoExistente, productoRequestDTO);
        return guardarProducto(productoExistente);
    }

    private void actualizarProductoExistente(Producto productoExistente, ProductoRequestDTO productoRequestDTO) {
        productoExistente.setCategoria(productoRequestDTO.getCategoria());
        productoExistente.setDescripcion(productoRequestDTO.getDescripcion());
        if (productoRequestDTO.getImageData() != null) {
            productoExistente.setImageData(Base64.getDecoder().decode(productoRequestDTO.getImageData()));
        }
        productoExistente.setInventario(productoRequestDTO.getInventario());
        productoExistente.setNombre(productoRequestDTO.getNombre());
        productoExistente.setPrecioVenta(productoRequestDTO.getPrecioVenta());
    }

    private ProductoRequestDTO guardarProducto(Producto producto) {
        try {
            Producto productoGuardado = productoRepository.save(producto);
            return convertToDTO(productoGuardado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InternalServerErrorException(ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG);
        }
    }

    private Producto obtenerProductoPorIdHeper (Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.PRODUCTO_NOT_FOUND);
            });
    }

    private ProductoRequestDTO convertToDTO(Producto producto) {
        ProductoRequestDTO dto = mapper.map(producto, ProductoRequestDTO.class);
        if (producto.getImageData() != null) {
            dto.setImageData(Base64.getEncoder().encodeToString(producto.getImageData()));
        }
        return dto;
    }
    
}

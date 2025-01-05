package com.web.aldalu.aldalu.services;

import java.util.List;
import java.util.Optional;

import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;

public interface ITiendaService {
    List<Tienda> obtenerTiendas();
    Tienda obtenerTiendaPorId(Long id);
    Tienda guardarTienda(TiendaDTO tienda);
    Optional<Tienda> eliminarTienda(Tienda tienda);
    List<Producto> obtenerProductosPorTienda(Long tiendaId);
}

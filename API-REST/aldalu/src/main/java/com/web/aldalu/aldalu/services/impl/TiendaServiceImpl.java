package com.web.aldalu.aldalu.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.repositories.ITiendaRepository;
import com.web.aldalu.aldalu.services.ITiendaService;

@Service
public class TiendaServiceImpl implements ITiendaService  {

    @Autowired
    ITiendaRepository tiendaRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Tienda> obtenerTiendas() {
        return tiendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Tienda> obtenerTiendaPorId(Long id) {
        return tiendaRepository.findById(id);
    }

    @Override
    public Tienda guardarTienda(Tienda tienda) {
        return tiendaRepository.save(tienda);
    }

    @Override
    public Optional<Tienda> eliminarTienda(Tienda tienda) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(tienda.getId());
        tiendaOptional.ifPresent(tiendaDb -> {
            tiendaRepository.delete(tienda);
        });
        return tiendaOptional;
    }

    @Transactional
    @Override
    public List<Producto> obtenerProductosPorTienda(Long tiendaId) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(tiendaId);
        if(tiendaOptional.isPresent()) {
            return tiendaOptional.get().getProductos();
        } else {
            return Collections.emptyList();
        }
    }

    
}

package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.PedidoTienda;

public interface IPedidoTiendaRepository extends JpaRepository<PedidoTienda, Long> {
    
}

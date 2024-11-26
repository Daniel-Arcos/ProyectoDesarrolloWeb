package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
    
}

package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.Tienda;

public interface ITiendaRepository extends JpaRepository<Tienda, Long> {
    
}

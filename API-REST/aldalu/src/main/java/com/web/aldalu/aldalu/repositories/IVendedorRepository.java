package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.Vendedor;

public interface IVendedorRepository extends JpaRepository<Vendedor, Long> {
    
}

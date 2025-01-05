package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.aldalu.aldalu.models.entities.Vendedor;

@Repository
public interface IVendedorRepository extends JpaRepository<Vendedor, Long> {
    
}

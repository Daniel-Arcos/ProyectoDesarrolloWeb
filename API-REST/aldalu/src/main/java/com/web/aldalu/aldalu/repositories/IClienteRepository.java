package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.Cliente;

public interface IClienteRepository extends JpaRepository<Cliente, Long> {
    
}

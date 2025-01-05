package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.aldalu.aldalu.models.entities.Tarjeta;

@Repository
public interface ITarjetaRepository extends JpaRepository<Tarjeta, Long> {
    
}

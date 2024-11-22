package com.web.aldalu.aldalu.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.aldalu.aldalu.models.enums.EmisorTarjeta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarjeta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String anoVencimiento;
    private String mesVencimiento;
    private int codigoSeguridad;
    private EmisorTarjeta emisorTarjeta;
    private String nombreTitular;
    private String numeroTarjeta;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;
}

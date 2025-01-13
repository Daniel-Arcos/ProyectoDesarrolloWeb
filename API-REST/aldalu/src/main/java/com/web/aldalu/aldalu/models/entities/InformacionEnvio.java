package com.web.aldalu.aldalu.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionEnvio {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String colonia;
    private String ciudad;
    private int codigoPostal;
    private String nombreCompleto;
    
    public InformacionEnvio(Direccion direccion) {
        this.calle = direccion.getCalle();
        this.numeroExterior = direccion.getNumeroExterior();
        this.numeroInterior = direccion.getNumeroInterior();
        this.colonia = direccion.getColonia();
        this.ciudad = direccion.getCiudad();
        this.codigoPostal = direccion.getCodigoPostal();
        this.nombreCompleto = direccion.getNombreCompleto();
    }
}

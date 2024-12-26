package com.web.aldalu.aldalu.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String calle;
    private String ciudad;
    private int codigoPostal;
    private String colonia;
    private String entidadFederativa;
    private String municipio;
    private String numeroExterior;
    private String numeroInterior;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordenadasDireccion_id", referencedColumnName = "id")
    private CoordenadasDireccion coordenadasDireccion;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;
}

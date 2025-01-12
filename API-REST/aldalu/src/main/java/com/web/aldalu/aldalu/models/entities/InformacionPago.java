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
public class InformacionPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    private String numeroTarjeta;
    private String emisorTarjeta;
    private String nombreTitular;

    public InformacionPago(Tarjeta tarjeta) {
        this.numeroTarjeta = tarjeta.getNumeroTarjeta();
        this.emisorTarjeta = tarjeta.getEmisorTarjeta().toString();
        this.nombreTitular = tarjeta.getNombreTitular();
    }
}

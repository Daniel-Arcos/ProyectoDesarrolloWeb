package com.web.aldalu.aldalu.models;

import jakarta.persistence.Entity;

@Entity
public class Vendedor extends Usuario {
    
    private String nombreVendedor;
    private String telefonoCelular;

    public String getNombreVendedor() {
        return nombreVendedor;
    }
    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    public String getTelefonoCelular() {
        return telefonoCelular;
    }
    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }
    
}

package com.web.aldalu.aldalu.models.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor extends Usuario {
    
    private String nombreVendedor;
    private String telefonoCelular;
    
}

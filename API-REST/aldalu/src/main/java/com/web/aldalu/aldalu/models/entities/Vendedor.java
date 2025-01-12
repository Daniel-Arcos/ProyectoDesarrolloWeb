package com.web.aldalu.aldalu.models.entities;

import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor extends Usuario {

    private String nombreVendedor;
    private String telefonoCelular;
    private LocalDate fechaNacimiento;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    private Tienda tienda;
}

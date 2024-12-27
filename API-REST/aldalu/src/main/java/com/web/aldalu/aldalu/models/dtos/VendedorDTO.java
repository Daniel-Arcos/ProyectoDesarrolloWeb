package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VendedorDTO extends UsuarioDTO {
    private String nombreVendedor;
    private String telefonoCelular;
}

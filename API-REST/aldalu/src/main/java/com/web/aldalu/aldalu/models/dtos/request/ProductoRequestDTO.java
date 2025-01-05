package com.web.aldalu.aldalu.models.dtos.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.entities.Resena;
import com.web.aldalu.aldalu.models.enums.Categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {
    private Long id;
    private String nombre;
    private Categoria categoria;
    private String descripcion;
    private float inventario;
    private float precioVenta;
    private Long tiendaId;
    private List<Resena> resenas;
}

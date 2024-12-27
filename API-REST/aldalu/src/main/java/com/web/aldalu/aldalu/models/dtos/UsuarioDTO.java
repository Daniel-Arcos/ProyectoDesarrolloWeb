package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private TipoUsuario tipoUsuario;
}

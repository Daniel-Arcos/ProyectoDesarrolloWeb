package com.web.aldalu.aldalu.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {
    private Long id;
    private String correoElectronico;
    private TipoUsuario tipoUsuario;
}

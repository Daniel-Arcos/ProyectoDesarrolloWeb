package com.web.aldalu.aldalu.models.dtos.request;

import java.time.LocalDate;

import com.web.aldalu.aldalu.models.enums.TipoUsuario;
import com.web.aldalu.aldalu.utils.PasswordConstraint;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RegisterRequestDTO {
    private String nombre;
    private String telefonoCelular;
    private LocalDate fechaNacimiento;
    private String email;

    @PasswordConstraint
    private String password;
    private TipoUsuario tipoUsuario;
}

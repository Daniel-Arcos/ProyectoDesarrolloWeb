package com.web.aldalu.aldalu.models.dtos.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;
import com.web.aldalu.aldalu.utils.PasswordConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String nombre;
    private String telefonoCelular;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String email;

    @PasswordConstraint
    private String password;
    private TipoUsuario tipoUsuario;

    @Override
    public String toString() {
        return "RegisterRequestDTO{" +
            "nombre='" + nombre + '\'' +
            ", telefonoCelular='" + telefonoCelular + '\'' +
            ", fechaNacimiento=" + fechaNacimiento +
            ", email='" + email + '\'' +
            ", tipoUsuario=" + tipoUsuario +
            '}';
    }
}

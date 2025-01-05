package com.web.aldalu.aldalu.models.dtos.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDate fechaNacimiento;
    private String email;

    @PasswordConstraint
    private String password;
    private TipoUsuario tipoUsuario;
}

package com.web.aldalu.aldalu.models.dtos.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.aldalu.aldalu.models.enums.RolEmpleado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorResponseDTO {
    private Long id;
    private String email;
    private String nombreEmpleado;
    private LocalDate fechaIngreso;
    private RolEmpleado rolEmpleado;
}

package com.web.aldalu.aldalu.models.dtos.response;

import com.web.aldalu.aldalu.models.enums.RolEmpleado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoResponseDTO {
    private Long id;
    private String nombreEmpleado;
    private RolEmpleado rolEmpleado;
}

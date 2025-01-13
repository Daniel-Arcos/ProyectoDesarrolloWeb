package com.web.aldalu.aldalu.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.aldalu.aldalu.models.dtos.UsuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    @JsonProperty("Token")
    private String accessToken;

    @JsonProperty("RefreshToken")
    private String refreshToken;

    private UsuarioDTO usuario;
}

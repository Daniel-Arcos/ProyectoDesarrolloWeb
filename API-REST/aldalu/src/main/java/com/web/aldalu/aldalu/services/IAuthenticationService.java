package com.web.aldalu.aldalu.services;

import java.io.IOException;

import com.web.aldalu.aldalu.models.dtos.request.AuthenticationRequestDTO;
import com.web.aldalu.aldalu.models.dtos.request.RegisterRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.AuthenticationResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthenticationService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

package com.web.aldalu.aldalu.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.aldalu.aldalu.models.dtos.request.AuthenticationRequestDTO;
import com.web.aldalu.aldalu.models.dtos.request.RegisterRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.AuthenticationResponseDTO;
import com.web.aldalu.aldalu.services.impl.AuthenticationServiceImpl;
import com.web.aldalu.aldalu.utils.EndpointsConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_AUTH)
@RequiredArgsConstructor
public class AuthenticationController {
   private final AuthenticationServiceImpl service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
    AuthenticationResponseDTO response = service.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
    AuthenticationResponseDTO response = service.authenticate(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    service.refreshToken(request, response);
  }
    
}

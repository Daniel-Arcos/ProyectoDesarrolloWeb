package com.web.aldalu.aldalu.services.impl;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.aldalu.aldalu.models.dtos.UsuarioDTO;
import com.web.aldalu.aldalu.models.dtos.request.AuthenticationRequestDTO;
import com.web.aldalu.aldalu.models.dtos.request.RegisterRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.AuthenticationResponseDTO;
import com.web.aldalu.aldalu.models.entities.Cliente;
import com.web.aldalu.aldalu.models.entities.Usuario;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.repositories.IClienteRepository;
import com.web.aldalu.aldalu.repositories.IEmpleadoAlmacenRepository;
import com.web.aldalu.aldalu.repositories.IUsuarioRepository;
import com.web.aldalu.aldalu.repositories.IVendedorRepository;
import com.web.aldalu.aldalu.security.services.JwtService;
import com.web.aldalu.aldalu.services.IAuthenticationService;
import com.web.aldalu.aldalu.utils.AuthConstants;
import com.web.aldalu.aldalu.utils.enums.Role;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IVendedorRepository vendedorRepository;
    private final IClienteRepository clienteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IEmpleadoAlmacenRepository empleadoAlmacenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        Usuario usuario = crearUsuarioDesdePeticion(request);
        guardarUsuario(usuario);
        
        String jwtToken = jwtService.generateToken(usuario);
        String jwtRefreshToken = jwtService.generateRefreshToken(usuario);
        
        return construirAuthResponse(jwtToken, jwtRefreshToken, usuario);
    }

    private Usuario crearUsuarioDesdePeticion(RegisterRequestDTO request) {
        return switch (request.getTipoUsuario()) {
            case CLIENTE -> crearClienteDesdePeticion(request);
            case VENDEDOR -> crearVendedorDesdePeticion(request);
            default -> throw new IllegalArgumentException("Tipo de usuario no soportado");
        };
    }

    private void guardarUsuario(Usuario usuario) {
        if (usuario instanceof Cliente) {
            clienteRepository.save((Cliente) usuario);
        } else if (usuario instanceof Vendedor) {
            vendedorRepository.save((Vendedor) usuario);
        }
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        authenticateUser(request.getEmail(), request.getPassword());
        String jwtToken = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);
        return construirAuthResponse(jwtToken, refreshToken, usuario);
        
    }

    private void authenticateUser(String correoElectronico, String contrasena) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(correoElectronico, contrasena)
        );
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(isInvalidAuthHeader(authHeader)) {
            return;
        }

        String refreshToken = extractToken(authHeader);
        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail!= null){
            processRefreshToken(response, refreshToken, userEmail);
        }
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX);
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(AuthConstants.BEARER_PREFIX.length());
    }

    private void processRefreshToken(HttpServletResponse response, String refreshToken, String userEmail) throws IOException {
        Usuario user = usuarioRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshToken, user)) {
          String accessToken = jwtService.generateToken(user);
          writeAuthResponse(response, construirAuthResponse(accessToken, refreshToken, user));
        }
    }

    private void writeAuthResponse(HttpServletResponse response, AuthenticationResponseDTO authResponse) throws IOException {
        response.setContentType(AuthConstants.CONTENT_TYPE_JSON);
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    private Cliente crearClienteDesdePeticion(RegisterRequestDTO request) {
        return Cliente.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .tipoUsuario(request.getTipoUsuario())
            .fechaNacimiento(request.getFechaNacimiento())
            .nombre(request.getNombre())
            .telefonoCelular(request.getTelefonoCelular())
            .role(Role.USER)
            .build();       
    }

    private Vendedor crearVendedorDesdePeticion(RegisterRequestDTO request) {
        return Vendedor.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .tipoUsuario(request.getTipoUsuario())
            .fechaNacimiento(request.getFechaNacimiento())
            .nombreVendedor(request.getNombre())
            .telefonoCelular(request.getTelefonoCelular())
            .role(Role.USER)
            .build();       
    }

    private AuthenticationResponseDTO construirAuthResponse(String jwtToken, String refreshToken, Usuario usuario) {
        return AuthenticationResponseDTO.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .usuario(construirUsuarioDTO(usuario))
        .build();
    }

    private UsuarioDTO construirUsuarioDTO(Usuario usuario) {
        var detallesUsuario = obtenerDetallesUsuario(usuario);
    
        return UsuarioDTO.builder()
            .id(usuario.getId())
            .email(usuario.getEmail())
            .tipoUsuario(usuario.getTipoUsuario())
            .telefonoCelular(detallesUsuario.telefonoCelular())
            .nombre(detallesUsuario.nombre())
            .fechaNacimiento(detallesUsuario.fechaNacimiento())
            .build();
    }
    
    private DetallesUsuario obtenerDetallesUsuario(Usuario usuario) {
        return switch (usuario.getTipoUsuario()) {
            case CLIENTE -> clienteRepository.findById(usuario.getId())
                .map(cliente -> new DetallesUsuario(
                    cliente.getNombre(),
                    cliente.getTelefonoCelular(),
                    cliente.getFechaNacimiento()))
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
            case VENDEDOR -> vendedorRepository.findById(usuario.getId())
                .map(vendedor -> new DetallesUsuario(
                    vendedor.getNombreVendedor(),
                    vendedor.getTelefonoCelular(),
                    vendedor.getFechaNacimiento()))
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado"));
            case EMPLEADO_ALMACEN -> empleadoAlmacenRepository.findById(usuario.getId())
                .map(empleado -> new DetallesUsuario(
                    empleado.getNombreEmpleado(), "", null))
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));
            default -> throw new IllegalArgumentException("Tipo de usuario no soportado");
        };
    }
    
    private record DetallesUsuario(String nombre, String telefonoCelular, LocalDate fechaNacimiento) {}    
    
}

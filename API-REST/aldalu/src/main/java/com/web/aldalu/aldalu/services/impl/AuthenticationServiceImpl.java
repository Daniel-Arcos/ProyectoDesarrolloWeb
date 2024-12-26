package com.web.aldalu.aldalu.services.impl;

import java.io.IOException;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web.Client;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.aldalu.aldalu.models.dtos.request.AuthenticationRequestDTO;
import com.web.aldalu.aldalu.models.dtos.request.RegisterRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.AuthenticationResponseDTO;
import com.web.aldalu.aldalu.models.entities.Cliente;
import com.web.aldalu.aldalu.models.entities.EmpleadoAlmacen;
import com.web.aldalu.aldalu.models.entities.Usuario;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;
import com.web.aldalu.aldalu.repositories.IUsuarioRepository;
import com.web.aldalu.aldalu.repositories.IVendedorRepository;
import com.web.aldalu.aldalu.security.services.JwtService;
import com.web.aldalu.aldalu.services.IAuthenticationService;
import com.web.aldalu.aldalu.utils.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IVendedorRepository vendedorRepository;
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        String jwtToken = "";
        String jwtRefreshToken = "";
        if(request.getTipoUsuario() == TipoUsuario.CLIENTE) {
            Cliente usuario = crearClienteDesdePeticion(request);
            ///guardar
        } else if (request.getTipoUsuario() == TipoUsuario.VENDEDOR) {
            Vendedor usuario = crearVendedorDesdePeticion(request);
            vendedorRepository.save(usuario);
            jwtToken = jwtService.generateToken(usuario);
            jwtRefreshToken = jwtService.generateRefreshToken(usuario);
        }
        return construirAuthResponse(jwtToken, jwtRefreshToken);
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        authenticateUser(request.getEmail(), request.getPassword());
        String jwtToken = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);
        return construirAuthResponse(jwtToken, refreshToken);
        
    }

    private void authenticateUser(String correoElectronico, String contrasena) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(correoElectronico, contrasena)
        );
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
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

    private AuthenticationResponseDTO construirAuthResponse(String jwtToken, String refreshToken) {
        return AuthenticationResponseDTO.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }
    
}

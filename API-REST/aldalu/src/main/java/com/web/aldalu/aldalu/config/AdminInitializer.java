package com.web.aldalu.aldalu.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.web.aldalu.aldalu.models.entities.EmpleadoAlmacen;
import com.web.aldalu.aldalu.models.enums.RolEmpleado;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;
import com.web.aldalu.aldalu.repositories.IUsuarioRepository;
import com.web.aldalu.aldalu.utils.enums.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByRole(Role.ADMIN).isEmpty()) {
            EmpleadoAlmacen admin = EmpleadoAlmacen.builder()
                .nombreEmpleado("Administrador")
                .email("admin@aldalu.com")
                .password(passwordEncoder.encode("admin123456"))
                .fechaIngreso(LocalDate.now())
                .rolEmpleado(RolEmpleado.ADMINISTRADOR)
                .tipoUsuario(TipoUsuario.EMPLEADO_ALMACEN)
                .role(Role.ADMIN)
                .build();
            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado exitosamente");
        } else {
            System.out.println("El usuario administrador ya existe");
        }
    }
    
}

package com.web.aldalu.aldalu.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.RepartidorRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.EmpleadoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseRepartidorDTO;
import com.web.aldalu.aldalu.models.dtos.response.RepartidorResponseDTO;
import com.web.aldalu.aldalu.models.entities.EmpleadoAlmacen;
import com.web.aldalu.aldalu.models.entities.Pedido;
import com.web.aldalu.aldalu.models.enums.RolEmpleado;
import com.web.aldalu.aldalu.models.enums.TipoUsuario;
import com.web.aldalu.aldalu.repositories.IEmpleadoAlmacenRepository;
import com.web.aldalu.aldalu.services.IEmpleadoService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;
import com.web.aldalu.aldalu.utils.enums.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements IEmpleadoService {

    private final IEmpleadoAlmacenRepository empleadoAlmacenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<RepartidorResponseDTO> obtenerRepartidores() {
       List<EmpleadoAlmacen> repartidores = empleadoAlmacenRepository
            .findAll()
                .stream()
                .filter(empleado -> empleado.getRolEmpleado() == RolEmpleado.REPARTIDOR)
                .collect(Collectors.toList());
        return convertirRepartidoresARepartidoresDTO(repartidores);
    }

    private List<RepartidorResponseDTO> convertirRepartidoresARepartidoresDTO (List<EmpleadoAlmacen> repartidores) {
        List<RepartidorResponseDTO> repartidoresResponse = new ArrayList<>();
        repartidores.forEach(repartidor -> {
            RepartidorResponseDTO repartidorResponseDTO = RepartidorResponseDTO.builder()
                .email(repartidor.getEmail())
                .fechaIngreso(repartidor.getFechaIngreso())
                .id(repartidor.getId())
                .nombreEmpleado(repartidor.getNombreEmpleado())
                .rolEmpleado(repartidor.getRolEmpleado())
                .build();
            repartidoresResponse.add(repartidorResponseDTO);
        });
        return repartidoresResponse;
    }

    @Override
    public RepartidorResponseDTO guardarRepartidor(RepartidorRequestDTO repartidor) {
        EmpleadoAlmacen repartidorGuardado = EmpleadoAlmacen.builder()
                .nombreEmpleado(repartidor.getNombreEmpleado())
                .email(repartidor.getEmail())
                .password(passwordEncoder.encode(repartidor.getPassword()))
                .fechaIngreso(LocalDate.now())
                .rolEmpleado(RolEmpleado.REPARTIDOR)
                .tipoUsuario(TipoUsuario.EMPLEADO_ALMACEN)
                .role(Role.USER)
                .build();
        return convertirEmpleadoARepartidor(empleadoAlmacenRepository.save(repartidorGuardado));
    }

    private RepartidorResponseDTO convertirEmpleadoARepartidor(EmpleadoAlmacen empleadoAlmacen) {
        return RepartidorResponseDTO.builder()
            .id(empleadoAlmacen.getId())
            .email(empleadoAlmacen.getEmail())
            .fechaIngreso(empleadoAlmacen.getFechaIngreso())
            .nombreEmpleado(empleadoAlmacen.getNombreEmpleado())
            .rolEmpleado(empleadoAlmacen.getRolEmpleado())
            .build();
    }

    @Override
    public List<PedidoResponseRepartidorDTO> obtenerPedidosRepartidor(Long id) {
        EmpleadoAlmacen repartidor = obtenerEmpleadoAlmacenPorIdHelper(id);
        return convertirPedidosADTO(repartidor.getPedidos());
    }

    private List<PedidoResponseRepartidorDTO> convertirPedidosADTO(List<Pedido> pedidos) {
        List<PedidoResponseRepartidorDTO> pedidosResponse = new ArrayList<>();
        pedidos.forEach(pedido -> {
            pedidosResponse.add(
                PedidoResponseRepartidorDTO.builder()
                    .id(pedido.getId())
                    .estadoPedido(pedido.getEstadoPedido())
                    .fechaCreacion(pedido.getFechaCreacion())
                    .informacionEnvio(pedido.getInformacionEnvio())
                    .build()
            );
        });
        return pedidosResponse;
    }

    private EmpleadoAlmacen obtenerEmpleadoAlmacenPorIdHelper (Long id) {
        return empleadoAlmacenRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.EMPLEADO_NOT_FOUND);
            });
    }

    @Override
    public EmpleadoResponseDTO obtenerEmpleadoPorId(Long id) {
       EmpleadoAlmacen empleadoAlmacen = obtenerEmpleadoAlmacenPorIdHelper(id);
       return EmpleadoResponseDTO.builder()
            .id(empleadoAlmacen.getId())
            .nombreEmpleado(empleadoAlmacen.getNombreEmpleado())
            .rolEmpleado(empleadoAlmacen.getRolEmpleado())
            .build();
    }
    
}

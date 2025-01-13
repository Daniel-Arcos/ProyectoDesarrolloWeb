package com.web.aldalu.aldalu.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.InternalServerErrorException;
import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.DireccionDTO;
import com.web.aldalu.aldalu.models.dtos.request.ClienteDTO;
import com.web.aldalu.aldalu.models.dtos.request.TarjetaRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseDTO;
import com.web.aldalu.aldalu.models.dtos.response.TarjetaDTO;
import com.web.aldalu.aldalu.models.entities.Cliente;
import com.web.aldalu.aldalu.models.entities.CoordenadasDireccion;
import com.web.aldalu.aldalu.models.entities.Direccion;
import com.web.aldalu.aldalu.models.entities.Pedido;
import com.web.aldalu.aldalu.models.entities.Tarjeta;
import com.web.aldalu.aldalu.repositories.IClienteRepository;
import com.web.aldalu.aldalu.repositories.ICoordenadaDireccionRepository;
import com.web.aldalu.aldalu.repositories.IDireccionRepository;
import com.web.aldalu.aldalu.repositories.ITarjetaRepository;
import com.web.aldalu.aldalu.services.IClienteService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private final IClienteRepository clienteRepository;
    private final ITarjetaRepository tarjetaRepository;
    private final IDireccionRepository direccionRepository;
    private final ICoordenadaDireccionRepository coordenadaDireccionRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<TarjetaDTO> obtenerTarjetasCliente(Long idCliente) {
        Cliente cliente = obtenerClientePorIdHeper(idCliente);
        return cliente.getTarjetas()
                .stream()
                .map(this::convertTarjetaToRest)
                .toList();

    }

    @Transactional
    @Override
    public List<DireccionDTO> obtenerDireccionesCliente(Long idCliente) {
        Cliente cliente = obtenerClientePorIdHeper(idCliente);
        return cliente.getDirecciones()
                .stream()
                .map(this::convertDireccionToRest)
                .toList();
    }

    @Transactional
    @Override
    public TarjetaDTO guardarTarjetaCliente(Long idCliente, TarjetaRequestDTO tarjetaDTO) {
        Cliente cliente = obtenerClientePorIdHeper(idCliente);
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNombreTitular(tarjetaDTO.getNombreTitular());
        tarjeta.setNumeroTarjeta(tarjetaDTO.getNumeroTarjeta());
        tarjeta.setMesVencimiento(tarjetaDTO.getMesVencimiento());
        tarjeta.setAnoVencimiento(tarjetaDTO.getAnoVencimiento());
        tarjeta.setEmisorTarjeta(tarjetaDTO.getEmisorTarjeta());
        tarjeta.setCodigoSeguridad(tarjetaDTO.getCodigoSeguridad());
        tarjeta.setCliente(cliente);
        return convertTarjetaToRest(tarjetaRepository.save(tarjeta));
    }

    @Transactional
    @Override
    public DireccionDTO guardarDireccionCliente(Long idCliente, DireccionDTO direccionDTO) {
        Cliente cliente = obtenerClientePorIdHeper(idCliente);
        Direccion direccion = new Direccion();
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumeroExterior(direccionDTO.getNumeroExterior());
        direccion.setNumeroInterior(direccionDTO.getNumeroInterior());
        direccion.setColonia(direccionDTO.getColonia());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());
        direccion.setCiudad(direccionDTO.getCiudad());
        direccion.setMunicipio(direccionDTO.getMunicipio());
        direccion.setEntidadFederativa(direccionDTO.getEntidadFederativa());
        direccion.setNombreCompleto(direccionDTO.getNombreCompleto());
        direccion.setTelefonoCelular(direccionDTO.getTelefonoCelular());

        CoordenadasDireccion coordenadasDireccion = new CoordenadasDireccion();
        coordenadasDireccion.setLatitud(direccionDTO.getCoordenadasDireccion().getLatitud());
        coordenadasDireccion.setLongitud(direccionDTO.getCoordenadasDireccion().getLongitud());

        direccion.setCoordenadasDireccion(coordenadaDireccionRepository.save(coordenadasDireccion));
        direccion.setCliente(cliente);
        return convertDireccionToRest(direccionRepository.save(direccion));
    }

    private Cliente obtenerClientePorIdHeper(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessageConstants.CLIENTE_NOT_FOUND);
                });
    }

    private TarjetaDTO convertTarjetaToRest(Tarjeta tarjeta) {
        return mapper.map(tarjeta, TarjetaDTO.class);
    }

    private DireccionDTO convertDireccionToRest(Direccion direccion) {
        return mapper.map(direccion, DireccionDTO.class);
    }

    @Override
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = obtenerClientePorIdHeper(id);
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setTelefonoCelular(clienteDTO.getTelefonoCelular());
        cliente.setFechaNacimiento(clienteDTO.getFechaNacimiento());
        return guardarCliente(cliente);
    }

    private ClienteDTO guardarCliente(Cliente cliente) {
        try {
            Cliente clienteGuardado = clienteRepository.save(cliente);
            return convertToDTO(clienteGuardado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InternalServerErrorException(ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG);
        }
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        return mapper.map(cliente, ClienteDTO.class);
    }

    @Override
    public void eliminarDireccion(Long idDireccion) {
        Direccion direccion = obtenerDireccionPorIdHelper(idDireccion);
        direccionRepository.delete(direccion);
    }

    private Direccion obtenerDireccionPorIdHelper(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessageConstants.DIRECCION_NOT_FOUND);
                });
    }

    @Override
    public DireccionDTO actualizarDirecccion(Long id, DireccionDTO direccionDTO) {
        Direccion direccionExistente = obtenerDireccionPorIdHelper(id);
        actualizarDireccionExistente(direccionExistente, direccionDTO);
        return convertDireccionToRest(direccionRepository.save(direccionExistente));
    }

    private void actualizarDireccionExistente(Direccion direccion, DireccionDTO direccionDTO) {
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumeroExterior(direccionDTO.getNumeroExterior());
        direccion.setNumeroInterior(direccionDTO.getNumeroInterior());
        direccion.setColonia(direccionDTO.getColonia());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());
        direccion.setCiudad(direccionDTO.getCiudad());
        direccion.setMunicipio(direccionDTO.getMunicipio());
        direccion.setEntidadFederativa(direccionDTO.getEntidadFederativa());
        direccion.setNombreCompleto(direccionDTO.getNombreCompleto());
        direccion.setTelefonoCelular(direccionDTO.getTelefonoCelular());
        direccion.getCoordenadasDireccion().setLatitud(direccionDTO.getCoordenadasDireccion().getLatitud());
        direccion.getCoordenadasDireccion().setLongitud(direccionDTO.getCoordenadasDireccion().getLongitud());
    }

    @Override
    public void eliminarTarjeta(Long idTarjeta) {
        Tarjeta tarjeta = obtenerTarjetaPorIdHelper(idTarjeta);
        tarjetaRepository.delete(tarjeta);
    }

    private Tarjeta obtenerTarjetaPorIdHelper(Long id) {
        return tarjetaRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessageConstants.TARJETA_NOT_FOUND);
                });
    }

    @Override
    public List<PedidoResponseDTO> obtenerPedidosCliente(Long idCliente) {
        Cliente cliente = obtenerClientePorIdHeper(idCliente);
        return convertirPedidosAPedidosResponseDTO(cliente.getPedidos());
    }

    private List<PedidoResponseDTO> convertirPedidosAPedidosResponseDTO(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> {
                    PedidoResponseDTO dto = new PedidoResponseDTO();

                    dto.setId(pedido.getId());
                    dto.setFechaCreacion(pedido.getFechaCreacion());
                    dto.setEstadoPedido(pedido.getEstadoPedido());
                    dto.setTotal(pedido.getTotal().floatValue());

                    dto.setInformacionEnvio(pedido.getInformacionEnvio());
                    dto.setInformacionPago(pedido.getInformacionPago());

                    int cantidadTotal = pedido.getPedidosTienda().stream()
                            .flatMap(pedidoTienda -> pedidoTienda.getProductosPedido().stream())
                            .mapToInt(productoPedido -> (int) productoPedido.getCantidad())
                            .sum();

                    dto.setCantidadProductos(cantidadTotal);

                    return dto;
                })
                .collect(Collectors.toList());
    }

}

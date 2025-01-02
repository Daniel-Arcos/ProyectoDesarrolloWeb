package com.web.aldalu.aldalu.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.DireccionDTO;
import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.dtos.VendedorDTO;
import com.web.aldalu.aldalu.models.dtos.request.TarjetaRequestDTO;
import com.web.aldalu.aldalu.models.dtos.response.TarjetaDTO;
import com.web.aldalu.aldalu.models.entities.Cliente;
import com.web.aldalu.aldalu.models.entities.CoordenadasDireccion;
import com.web.aldalu.aldalu.models.entities.Direccion;
import com.web.aldalu.aldalu.models.entities.Tarjeta;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.repositories.IClienteRepository;
import com.web.aldalu.aldalu.repositories.ICoordenadaDireccionRepository;
import com.web.aldalu.aldalu.repositories.IDireccionRepository;
import com.web.aldalu.aldalu.repositories.ITarjetaRepository;
import com.web.aldalu.aldalu.services.IClienteService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService{

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
        tarjeta.setCodigoSeguridad(tarjeta.getCodigoSeguridad());
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
}

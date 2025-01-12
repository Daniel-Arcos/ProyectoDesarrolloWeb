package com.web.aldalu.aldalu.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.dtos.VendedorDTO;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.repositories.IVendedorRepository;
import com.web.aldalu.aldalu.services.IVendedorService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendedorServiceImpl implements IVendedorService {
    
    private final IVendedorRepository vendedorRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<VendedorDTO> obtenerVendedores() {
        List<Vendedor> vendedores = vendedorRepository.findAll();
        return vendedores.stream().map(this::convertToRest).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<VendedorDTO> obtenerVendedorPorId(Long id) {
        Optional<Vendedor> vendedor = vendedorRepository.findById(id);
        return Optional.of(convertToRest(vendedor.get()));
    }

    @Transactional
    @Override
    public VendedorDTO guardarVendedor(@NonNull final VendedorDTO vendedorDTO) {
        vendedorDTO.setId(null);
        Vendedor vendedorEntity = convertToEntity(vendedorDTO);
        Vendedor vendedorGuardado = vendedorRepository.save(vendedorEntity);
        return convertToRest(vendedorGuardado);
    }

    @Override
    public Optional<VendedorDTO> actualizarVendedor(@NonNull Long vendedorId, @NonNull VendedorDTO vendedorDTO) {
        Optional<Vendedor> vendedor = vendedorRepository.findById(vendedorId);
        if (vendedor.isPresent()) {
            Vendedor vendedorEntity = convertToEntity(vendedorDTO);
            vendedorEntity.setId(vendedorId);
            Vendedor vendedorActualizado = vendedorRepository.save(vendedorEntity);
            return Optional.of(convertToRest(vendedorActualizado));
        } else {
            return Optional.of(new VendedorDTO());
        }

    }

    @Override
    public Optional<VendedorDTO> eliminarVendedor(@NonNull final Long id) {
        Optional<Vendedor> vendedorOptional = vendedorRepository.findById(id);
        vendedorOptional.ifPresent(productDb -> {
            vendedorRepository.delete(productDb);
        });
        return Optional.of(convertToRest(vendedorOptional.get()));
    }

    private VendedorDTO convertToRest(Vendedor vendedor) {
        return mapper.map(vendedor, VendedorDTO.class);
    }

    private Vendedor convertToEntity(VendedorDTO vendedorDTO) {
        return mapper.map(vendedorDTO, Vendedor.class);
    }

    private TiendaDTO convertTiendaToDTO(Tienda tienda) {
        return mapper.map(tienda, TiendaDTO.class);
    }

    @Override
    public TiendaDTO obtenerTiendaVendedor(Long id) {
        Vendedor vendedor = obtenerVendedorPorIdHelper(id);
        TiendaDTO tienda = convertTiendaToDTO(vendedor.getTienda());
        tienda.setIdVendedor(id);
        return tienda;
    }

    private Vendedor obtenerVendedorPorIdHelper (Long id) {
        return vendedorRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.VENDEDOR_NOT_FOUND);
            });
    }
}

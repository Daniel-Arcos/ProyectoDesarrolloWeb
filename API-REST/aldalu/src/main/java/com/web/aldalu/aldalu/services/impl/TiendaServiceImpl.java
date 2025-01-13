package com.web.aldalu.aldalu.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.TiendaDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoTiendaDTO;
import com.web.aldalu.aldalu.models.entities.PedidoTienda;
import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.models.entities.Vendedor;
import com.web.aldalu.aldalu.repositories.ITiendaRepository;
import com.web.aldalu.aldalu.repositories.IVendedorRepository;
import com.web.aldalu.aldalu.services.ITiendaService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TiendaServiceImpl implements ITiendaService  {
    
    private final IVendedorRepository vendedorRepository;
    private final ITiendaRepository tiendaRepository;
    

    @Transactional(readOnly = true)
    @Override
    public List<Tienda> obtenerTiendas() {
        return tiendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Tienda obtenerTiendaPorId(Long id) {
        Tienda tienda = obtenerTiendaPorIdHeper(id);
        return tienda;
    }

    @Override
    public Tienda guardarTienda(TiendaDTO tiendaDTO) {
        Vendedor vendedor = vendedorRepository.findById(tiendaDTO.getIdVendedor())
            .orElseThrow(()-> new NotFoundException(ExceptionMessageConstants.VENDEDOR_NOT_FOUND));
        Tienda tienda = new Tienda();
        tienda.setNombreTienda(tiendaDTO.getNombreTienda());
        tienda.setDescripcion(tiendaDTO.getDescripcion());
        tienda.setProductos(new ArrayList<>());
        tienda.setPedidos(new ArrayList<>());

        vendedor.setTienda(tienda);

        tiendaRepository.save(tienda);
        vendedorRepository.save(vendedor);

        return tienda;
    }

    @Override
    public Optional<Tienda> eliminarTienda(Tienda tienda) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(tienda.getId());
        tiendaOptional.ifPresent(tiendaDb -> {
            tiendaRepository.delete(tienda);
        });
        return tiendaOptional;
    }

    @Transactional
    @Override
    public List<Producto> obtenerProductosPorTienda(Long tiendaId) {
        Tienda tienda = obtenerTiendaPorIdHeper(tiendaId)   ;
        return tienda.getProductos();
    }

    private Tienda obtenerTiendaPorIdHeper (Long id) {
        return tiendaRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.TIENDA_NOT_FOUND);
            });
    }

    @Override
    public List<PedidoTiendaDTO> obtenerPedidosPorTienda(Long id) {
        Tienda tienda = obtenerTiendaPorId(id);
        return convertirPedidosTiendaAPedidoTiendaDTO(tienda.getPedidos());
    }

    private List<PedidoTiendaDTO> convertirPedidosTiendaAPedidoTiendaDTO(List<PedidoTienda> pedidos) {
        List<PedidoTiendaDTO> pedidosTiendaDTO = new ArrayList<>();
        pedidos.stream().forEach(p -> {
            PedidoTiendaDTO pedidoTiendaDTO = new PedidoTiendaDTO();
            pedidoTiendaDTO.setEstadoPedidoTienda(p.getEstado());
            pedidoTiendaDTO.setFechaCreacion(p.getFechaCreacion());
            pedidoTiendaDTO.setId(p.getId());
            pedidoTiendaDTO.setInformacionPago(p.getPedido().getInformacionPago());
            pedidoTiendaDTO.setNombreCliente(p.getPedido().getCliente().getNombre());
            pedidoTiendaDTO.setTotal(p.getSubtotal());
            pedidosTiendaDTO.add(pedidoTiendaDTO);
        });

        return pedidosTiendaDTO;
    }

    
}

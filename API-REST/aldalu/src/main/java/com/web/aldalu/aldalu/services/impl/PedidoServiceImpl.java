package com.web.aldalu.aldalu.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.aldalu.aldalu.exceptions.dtos.InsufficientInventaryException;
import com.web.aldalu.aldalu.exceptions.dtos.NotFoundException;
import com.web.aldalu.aldalu.models.dtos.request.ActualizarEstadoPedidoDTO;
import com.web.aldalu.aldalu.models.dtos.request.PedidoRequestDTO;
import com.web.aldalu.aldalu.models.dtos.request.ProductoPedidoDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoResponseAdminDTO;
import com.web.aldalu.aldalu.models.dtos.response.PedidoTiendaAdminDTO;
import com.web.aldalu.aldalu.models.dtos.response.ProductoPedidoResponseDTO;
import com.web.aldalu.aldalu.models.entities.Cliente;
import com.web.aldalu.aldalu.models.entities.Direccion;
import com.web.aldalu.aldalu.models.entities.EmpleadoAlmacen;
import com.web.aldalu.aldalu.models.entities.InformacionEnvio;
import com.web.aldalu.aldalu.models.entities.InformacionPago;
import com.web.aldalu.aldalu.models.entities.Pedido;
import com.web.aldalu.aldalu.models.entities.PedidoTienda;
import com.web.aldalu.aldalu.models.entities.Producto;
import com.web.aldalu.aldalu.models.entities.ProductoPedido;
import com.web.aldalu.aldalu.models.entities.Tarjeta;
import com.web.aldalu.aldalu.models.entities.Tienda;
import com.web.aldalu.aldalu.models.enums.EstadoPedido;
import com.web.aldalu.aldalu.models.enums.EstadoPedidoTienda;
import com.web.aldalu.aldalu.repositories.IClienteRepository;
import com.web.aldalu.aldalu.repositories.IDireccionRepository;
import com.web.aldalu.aldalu.repositories.IEmpleadoAlmacenRepository;
import com.web.aldalu.aldalu.repositories.IPedidoRepository;
import com.web.aldalu.aldalu.repositories.IPedidoTiendaRepository;
import com.web.aldalu.aldalu.repositories.IProductoRepository;
import com.web.aldalu.aldalu.repositories.ITarjetaRepository;
import com.web.aldalu.aldalu.repositories.ITiendaRepository;
import com.web.aldalu.aldalu.services.IPedidoService;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {
    private final IPedidoRepository pedidoRepository;
    private final IProductoRepository productoRepository;
    private final IClienteRepository clienteRepository;
    private final IDireccionRepository direccionRepository;
    private final ITarjetaRepository tarjetaRepository;
    private final ITiendaRepository tiendaRepository;
    private final IPedidoTiendaRepository pedidoTiendaRepository;
    private final IEmpleadoAlmacenRepository empleadoAlmacenRepository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<PedidoResponseAdminDTO> obtenerPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return convertirPedidosAPedidoAdmin(pedidos);
    }

    private List<PedidoResponseAdminDTO> convertirPedidosAPedidoAdmin(List<Pedido> pedidos) {
        List<PedidoResponseAdminDTO> pedidosResponse = new ArrayList<>();
        pedidos.forEach(pedido -> {
            List<PedidoTiendaAdminDTO> pedidosTienda = convertirPedidosTiendaAPedidoTiendaAdmin(pedido.getPedidosTienda());
            PedidoResponseAdminDTO pedidoResponse = PedidoResponseAdminDTO.builder()
                .id(pedido.getId())
                .estadoPedido(pedido.getEstadoPedido())
                .fechaCreacion(pedido.getFechaCreacion())
                .informacionEnvio(pedido.getInformacionEnvio())
                .informacionPago(pedido.getInformacionPago())
                .nombreCliente(pedido.getCliente().getNombre())
                .pedidosTienda(pedidosTienda)
                .cantidadProductos(pedidosTienda.stream().mapToInt(PedidoTiendaAdminDTO::getCantidadProductos).sum())
                .build();
            pedidosResponse.add(pedidoResponse);
        });

        return pedidosResponse;
    }

    private List<PedidoTiendaAdminDTO> convertirPedidosTiendaAPedidoTiendaAdmin(List<PedidoTienda> pedidosTienda) {
        List<PedidoTiendaAdminDTO> pedidosTiendaResponse = new ArrayList<>();
        pedidosTienda.forEach(pedidoTienda -> {
            PedidoTiendaAdminDTO pedidoTiendaAdminDTO = PedidoTiendaAdminDTO.builder()
                .id(pedidoTienda.getId())
                .cantidadProductos(pedidoTienda.getProductosPedido().size())
                .estadoPedidoTienda(pedidoTienda.getEstado())
                .nombreTienda(pedidoTienda.getTienda().getNombreTienda())
                .subtotal(pedidoTienda.getSubtotal())
                .build();
            pedidosTiendaResponse.add(pedidoTiendaAdminDTO);
        });
        return pedidosTiendaResponse;
    }

    private PedidoRequestDTO convertToDTO(Pedido pedido) {
        return mapper.map(pedido, PedidoRequestDTO.class);
    }

    @Override
    public PedidoRequestDTO guardarPedido(PedidoRequestDTO pedidoDTO) {
        Cliente cliente = obtenerClientePorIdHelper(pedidoDTO.getIdCliente());
        Tarjeta tarjeta = obtenerTarjetaPorIdHelper(pedidoDTO.getIdTarjeta());
        Direccion direccion = obtenerDireccionPorIdHelper(pedidoDTO.getIdDireccion());
        InformacionPago infoPago = new InformacionPago(tarjeta);
        InformacionEnvio infoEnvio = new InformacionEnvio(direccion);
        for (ProductoPedidoDTO prodDTO : pedidoDTO.getProductos()) {
            Producto producto = obtenerProductoPorIdHelper(prodDTO.getIdProducto());
            if (producto.getInventario() < prodDTO.getCantidad()) {
                throw new InsufficientInventaryException(
                    "Inventario insuficiente para el producto: " + producto.getNombre());
            }
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setInformacionPago(infoPago);
        pedido.setInformacionEnvio(infoEnvio);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);

        Map<Long, List<ProductoPedidoDTO>> productosPorTienda = pedidoDTO.getProductos()
            .stream()
            .collect(Collectors.groupingBy(ProductoPedidoDTO::getIdTienda));

        List<PedidoTienda> pedidosTienda = new ArrayList<>();
        double totalPedido = 0.0;

        for (Map.Entry<Long, List<ProductoPedidoDTO>> entry : productosPorTienda.entrySet()) {
            Long idTienda = entry.getKey();
            List<ProductoPedidoDTO> productosTiendaSeleccionada = entry.getValue();

            Tienda tienda = obtenerTiendaPorIdHelper(idTienda);

            PedidoTienda pedidoTienda = new PedidoTienda();
            pedidoTienda.setTienda(tienda);
            pedidoTienda.setPedido(pedido);
            pedidoTienda.setEstado(EstadoPedidoTienda.PENDIENTE_DE_CONFIRMACION);
            pedidoTienda.setFechaCreacion(LocalDateTime.now());

            List<ProductoPedido> productosPedidos = new ArrayList<>();
            double subtotalTienda = 0.0;

            for (ProductoPedidoDTO prod: productosTiendaSeleccionada) {
                Producto producto = obtenerProductoPorIdHelper(prod.getIdProducto());

                producto.setInventario(producto.getInventario() - prod.getCantidad());
                productoRepository.save(producto);
                
                ProductoPedido productoPedido = new ProductoPedido();
                productoPedido.setProducto(producto);
                productoPedido.setPedidoTienda(pedidoTienda);
                productoPedido.setCantidad(prod.getCantidad());
                productoPedido.setPrecioUnitario(prod.getPrecioUnitario());

                double subtotalProducto = prod.getCantidad() * prod.getPrecioUnitario();
                subtotalTienda += subtotalProducto;

                productosPedidos.add(productoPedido);
            }

            pedidoTienda.setProductosPedido(productosPedidos);
            pedidoTienda.setSubtotal(subtotalTienda);
            pedidosTienda.add(pedidoTienda);

            totalPedido += subtotalTienda;
        }

        pedido.setPedidosTienda(pedidosTienda);
        pedido.setTotal(totalPedido);

        return convertToDTO(pedidoRepository.save(pedido));
        
    }

    private Pedido obtenerPedidoPorIdHelper (Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.PEDIDO_NOT_FOUND);
            });
    }

    private PedidoTienda obtenerPedidoTiendaPorIdHelper (Long id) {
        return pedidoTiendaRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.PEDIDO_NOT_FOUND);
            });
    }

    private Tienda obtenerTiendaPorIdHelper (Long id) {
        return tiendaRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.TIENDA_NOT_FOUND);
            });
    }

    private Producto obtenerProductoPorIdHelper (Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.PRODUCTO_NOT_FOUND);
            });
    }

    private Cliente obtenerClientePorIdHelper (Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.CLIENTE_NOT_FOUND);
            });
    }

    private Direccion obtenerDireccionPorIdHelper (Long id) {
        return direccionRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.DIRECCION_NOT_FOUND);
            });
    }

    private Tarjeta obtenerTarjetaPorIdHelper (Long id) {
        return tarjetaRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.TARJETA_NOT_FOUND);
            });
    }

    private EmpleadoAlmacen obtenerEmpleadoAlmacenPorIdHelper (Long id) {
        return empleadoAlmacenRepository.findById(id)
            .orElseThrow(() -> {
                return new NotFoundException(ExceptionMessageConstants.EMPLEADO_NOT_FOUND);
            });
    }

    @Override
    public void actualizarEstadoPedidotienda(Long id, EstadoPedidoTienda estadoPedidoTienda) {
        PedidoTienda pedido = obtenerPedidoTiendaPorIdHelper(id);
        pedido.setEstado(estadoPedidoTienda);
        pedidoTiendaRepository.save(pedido);
    }

    @Override
    public void asignarRepartidorParaEnvio(Long id, ActualizarEstadoPedidoDTO estadoPedido) {
        Pedido pedido = obtenerPedidoPorIdHelper(id);
        EmpleadoAlmacen empleadoAlmacen = obtenerEmpleadoAlmacenPorIdHelper(estadoPedido.getIdRepartidor());
        pedido.setEstadoPedido(estadoPedido.getNuevoEstado());
        pedido.setRepartidor(empleadoAlmacen);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<ProductoPedidoResponseDTO> obtenerProductosPedido(Long id) {
        List<ProductoPedidoResponseDTO> productos = new ArrayList<>();
        Pedido pedido = obtenerPedidoPorIdHelper(id);
        pedido.getPedidosTienda().forEach(pedidoTienda -> {
            pedidoTienda.getProductosPedido().forEach(productoPedido -> {
                ProductoPedidoResponseDTO productoPedidoResponseDTO = 
                    ProductoPedidoResponseDTO.builder()
                        .cantidad(productoPedido.getCantidad())
                        .categoria(productoPedido.getProducto().getCategoria())
                        .descripcion(productoPedido.getProducto().getDescripcion())
                        .idProducto(productoPedido.getProducto().getId())
                        .imageData(productoPedido.getProducto().getImageData())
                        .nombre(productoPedido.getProducto().getNombre())
                        .precioUnitario(productoPedido.getPrecioUnitario())
                        .build();
                productos.add(productoPedidoResponseDTO);
            });
        });
        return productos;
    }

    @Override
    public List<ProductoPedidoResponseDTO> obtenerProductosPedidoTienda(Long id) {
        List<ProductoPedidoResponseDTO> productos = new ArrayList<>();
        PedidoTienda pedidoTienda = obtenerPedidoTiendaPorIdHelper(id);
        pedidoTienda.getProductosPedido().forEach(productoPedido -> {
            ProductoPedidoResponseDTO productoPedidoResponseDTO = 
                    ProductoPedidoResponseDTO.builder()
                        .cantidad(productoPedido.getCantidad())
                        .categoria(productoPedido.getProducto().getCategoria())
                        .descripcion(productoPedido.getProducto().getDescripcion())
                        .idProducto(productoPedido.getProducto().getId())
                        .imageData(productoPedido.getProducto().getImageData())
                        .nombre(productoPedido.getProducto().getNombre())
                        .precioUnitario(productoPedido.getPrecioUnitario())
                        .build();
                productos.add(productoPedidoResponseDTO);
        });
        return productos;
    }

    @Override
    public PedidoResponseAdminDTO obtenerPedidoPorId(Long id) {
       Pedido pedido = obtenerPedidoPorIdHelper(id);
       List<PedidoTiendaAdminDTO> pedidosTienda = convertirPedidosTiendaAPedidoTiendaAdmin(pedido.getPedidosTienda());
       PedidoResponseAdminDTO pedidoResponse = PedidoResponseAdminDTO.builder()
           .id(pedido.getId())
           .estadoPedido(pedido.getEstadoPedido())
           .fechaCreacion(pedido.getFechaCreacion())
           .informacionEnvio(pedido.getInformacionEnvio())
           .informacionPago(pedido.getInformacionPago())
           .nombreCliente(pedido.getCliente().getNombre())
           .pedidosTienda(pedidosTienda)
           .cantidadProductos(pedidosTienda.stream().mapToInt(PedidoTiendaAdminDTO::getCantidadProductos).sum())
           .idRepartidor(pedido.getRepartidor() != null ? pedido.getRepartidor().getId() : null)
           .build();
        return pedidoResponse;
    }

    @Override
    public void atualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPedidoPorIdHelper(id);
        pedido.setEstadoPedido(nuevoEstado);
        if (nuevoEstado == EstadoPedido.CANCELADO) {
            pedido.getPedidosTienda().forEach(pedidoTienda -> {
                pedidoTienda.setEstado(EstadoPedidoTienda.CANCELADO);
                pedidoTienda.getProductosPedido().forEach(producto -> {
                    producto.getProducto().setInventario(producto.getProducto().getInventario() + producto.getCantidad());
                });
                pedidoTiendaRepository.save(pedidoTienda);
            });
        }
        pedidoRepository.save(pedido);
    }

}

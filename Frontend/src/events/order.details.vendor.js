import { obtenerProductosPedidoTienda } from "../services/PedidosService.js";
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();
document.addEventListener('DOMContentLoaded', () => {
    const userData = JSON.parse(Cookies.get('userData'));
    if (userData && userData.nombre) {
        document.getElementById('user-name').textContent = userData.nombre;
    }    
});
function formatearFecha(fechaStr) {
    const fecha = new Date(fechaStr);
    return fecha.toLocaleDateString('es-ES');
}

function formatearMonto(monto) {
    return new Intl.NumberFormat('es-MX', {
        style: 'currency',
        currency: 'MXN'
    }).format(monto);
}

function obtenerEstadoInfo(estado) {
    const estados = {
        'PENDIENTE_DE_CONFIRMACION': {
            texto: 'Pendiente de confirmación',
            clase: 'bg-warning text-dark'
        },
        'EN_PREPARACION': {
            texto: 'En preparación',
            clase: 'bg-info text-dark'
        },
        'ENVIADO_AL_ALMACEN': {
            texto: 'Enviado al almacén',
            clase: 'bg-success'
        }
    };
    return estados[estado] || { texto: estado, clase: 'bg-secondary' };
}

function mostrarProducto(producto) {
    return `
        <div class="card mb-3">
            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-md-2">
                        <img src="data:image/jpeg;base64,${producto.imageData}" 
                             class="img-fluid rounded" 
                             alt="${producto.nombre}">
                    </div>
                    <div class="col-md-4">
                        <h6 class="mb-1">${producto.nombre}</h6>
                        <p class="text-muted small mb-0">${producto.descripcion}</p>
                        <span class="badge bg-secondary">${producto.categoria}</span>
                    </div>
                    <div class="col-md-2 text-center">
                        <p class="mb-0"><strong>Cantidad</strong></p>
                        <p class="mb-0">${producto.cantidad}</p>
                    </div>
                    <div class="col-md-2 text-center">
                        <p class="mb-0"><strong>Precio Unit.</strong></p>
                        <p class="mb-0">${formatearMonto(producto.precioUnitario)}</p>
                    </div>
                    <div class="col-md-2 text-center">
                        <p class="mb-0"><strong>Subtotal</strong></p>
                        <p class="mb-0">${formatearMonto(producto.cantidad * producto.precioUnitario)}</p>
                    </div>
                </div>
            </div>
        </div>
    `;
}

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const pedido = JSON.parse(sessionStorage.getItem('pedidoDetalle'));
        
        if (!pedido) {
            throw new Error('No hay información del pedido');
        }

        document.getElementById('pedidoId').textContent = pedido.id;
        document.getElementById('nombreCliente').textContent = pedido.nombreCliente;
        document.getElementById('fechaPedido').textContent = formatearFecha(pedido.fechaCreacion);
        
        const estadoInfo = obtenerEstadoInfo(pedido.estadoPedidoTienda);
        const estadoElement = document.getElementById('estadoPedido');
        estadoElement.textContent = estadoInfo.texto;
        estadoElement.className = `badge ${estadoInfo.clase}`;
        
        document.getElementById('totalPedido').textContent = formatearMonto(pedido.total);

        document.getElementById('titularTarjeta').textContent = pedido.informacionPago.nombreTitular;
        document.getElementById('ultimosDigitos').textContent = 
            pedido.informacionPago.numeroTarjeta.slice(-4);
        document.getElementById('emisorTarjeta').textContent = pedido.informacionPago.emisorTarjeta;

        const productos = await obtenerProductosPedidoTienda(pedido.id);
        
        const contenedorProductos = document.getElementById('listaProductos');
        contenedorProductos.innerHTML = '';
        productos.forEach(producto => {
            contenedorProductos.innerHTML += mostrarProducto(producto);
        });

        document.getElementById('totalProductos').textContent = 
            `${productos.length} ${productos.length === 1 ? 'producto' : 'productos'}`;

    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar los detalles del pedido');
    }
});
import { obtenerPedidosTienda } from '../services/TiendaService.js'
import { actualizarEstadoPedidoTienda } from '../services/PedidosService.js'
import { obtenerTiendaVendedor } from '../services/VendedorService.js';
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();

document.addEventListener('DOMContentLoaded', async function() {
    try {
        const userData = JSON.parse(Cookies.get('userData'));
        if (userData && userData.nombre) {
            document.getElementById('user-name').textContent = userData.nombre;
        }
        const vendedorId = userData.id;
        const tienda = await obtenerTiendaVendedor(vendedorId);
        const pedidos = await obtenerPedidosTienda(tienda.id);
        mostrarPedidos(pedidos);
    } catch (error) {
        console.error('Error al obtener productos:', error);
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

function mostrarPedidos(pedidos) {
    const containerPedidos = document.querySelector('.orders-list');
    containerPedidos.innerHTML = ''; 

    document.querySelector('h5').textContent = `${pedidos.length} pedidos totales`;

    pedidos.forEach(pedido => {
        const estadoInfo = obtenerEstadoInfo(pedido.estadoPedidoTienda);
        
        const cardHtml = `
            <div class="card mb-3" data-pedido-id="${pedido.id}">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-2">
                            <h5 class="card-title mb-2">Pedido #${pedido.id}</h5>
                            <p class="card-text mb-0">
                                <small class="text-muted">${formatearFecha(pedido.fechaCreacion)}</small>
                            </p>
                        </div>
                        <div class="col-md-2">
                            <p class="card-text mb-0">
                                <strong>Estado:</strong><br>
                                <span class="badge ${estadoInfo.clase}">${estadoInfo.texto}</span>
                            </p>
                        </div>
                        <div class="col-md-3">
                            <p class="card-text mb-0">
                                <strong>Cliente:</strong><br>
                                ${pedido.nombreCliente}
                            </p>
                        </div>
                        <div class="col-md-2">
                            <p class="card-text mb-0">
                                <strong>Total:</strong><br>
                                ${formatearMonto(pedido.total)}
                            </p>
                        </div>
                        <div class="col-md-3 text-end">
                            <button class="btn btn-outline-dark me-2 ver-detalles">
                                Ver detalles
                            </button>
                            <button class="btn btn-dark abrir-modal-btn">
                                Actualizar estado
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        containerPedidos.insertAdjacentHTML('beforeend', cardHtml);
        
        const ultimaCard = containerPedidos.lastElementChild;
        const abrirModalBtn = ultimaCard.querySelector(".abrir-modal-btn");
        const verDetallesBtn = ultimaCard.querySelector(".ver-detalles")
        abrirModalBtn.addEventListener('click', () => abrirModalEstado(pedido.id, pedido.estadoPedidoTienda));
        verDetallesBtn.addEventListener('click', () => verProductos(pedido))
    });
}

function abrirModalEstado(pedidoId, estadoActual) {
    const modal = document.getElementById('updateStatusModal');
    const estadoInfo = obtenerEstadoInfo(estadoActual);
    
    modal.querySelector('#currentStatus').value = estadoInfo.texto;
    modal.querySelector('#newStatus').value = estadoActual;
    modal.dataset.pedidoId = pedidoId;
    
    const modalInstance = new bootstrap.Modal(modal, {
        backdrop: 'static',
        keyboard: false
    });
    modalInstance.show();
}

async function actualizarEstado(event) {
    event.preventDefault();
    
    const modal = document.getElementById('updateStatusModal');
    const pedidoId = modal.dataset.pedidoId;
    const nuevoEstado = modal.querySelector('#newStatus').value;
    
    try {
        await actualizarEstadoPedidoTienda(pedidoId, nuevoEstado);
        const userData = JSON.parse(Cookies.get('userData'));
        const tienda = await obtenerTiendaVendedor(userData.id);
        const pedidos = await obtenerPedidosTienda(tienda.id);
        mostrarPedidos(pedidos);

        modal.querySelector('[data-bs-dismiss="modal"]').click();

    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el estado del pedido');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('updateStatusModal');
    const btnGuardar = document.querySelector('#updateStatusModal .btn-primary');
    btnGuardar.addEventListener('click', actualizarEstado);
    modal.addEventListener('hidden.bs.modal', function () {
        const backdrops = document.getElementsByClassName('modal-backdrop');
        while(backdrops.length > 0) {
            backdrops[0].remove();
        }
        document.body.classList.remove('modal-open');
        document.body.style.removeProperty('padding-right');
    });
    const botonesFilter = document.querySelectorAll('.btn-group .btn');
    botonesFilter.forEach(boton => {
        boton.addEventListener('click', async () => {
            botonesFilter.forEach(b => b.classList.remove('active'));
            boton.classList.add('active');

            try {
                const userData = JSON.parse(Cookies.get('userData'));
                const tienda = await obtenerTiendaVendedor(userData.id);
                let pedidos = await obtenerPedidosTienda(tienda.id);
                
                const filtro = boton.getAttribute('data-filter')
                if (filtro !== 'Todos') {
                    pedidos = pedidos.filter(p => p.estadoPedidoTienda === boton.getAttribute('data-filter'));
                }
                
                mostrarPedidos(pedidos);
            } catch (error) {
                console.error('Error al filtrar pedidos:', error);
            }
        });
    });
});

function verProductos(pedido) {
    sessionStorage.setItem('pedidoDetalle', JSON.stringify(pedido));
    window.location.href = './order-details.html';
}
import { obtenerPedidos } from "../services/ClienteService.js";
import { actualizarEstadoPedido } from "../services/PedidosService.js";

const formatearFecha = (fechaString) => {
    const fecha = new Date(fechaString);
    return fecha.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
};

const traducirEstado = (estado) => {
    const estados = {
        'EN_PREPARACION': 'En preparación',
        'ENVIADO': 'Enviado',
        'ENTREGADO': 'Entregado',
        'CANCELADO': 'Cancelado',
        'LISTO_PARA_ENVIO': 'Listo para envio'
    };
    return estados[estado] || estado;
};

const crearHTMLPedido = (pedido) => {
    const puedeSerCancelado = pedido.estadoPedido === 'EN_PREPARACION';
    
    const botonCancelar = puedeSerCancelado 
        ? `<button class="btn btn-outline-dark me-2 btn-cancelar" data-pedido-id="${pedido.id}">Cancelar</button>`
        : `<button class="btn btn-outline-dark me-2" disabled title="Solo se pueden cancelar pedidos en preparación">Cancelar</button>`;

    return `
        <div class="card mb-4">
            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-12 mb-3">
                        <h5 class="card-title mb-3">Pedido #${pedido.id}</h5>
                        <div class="row">
                            <div class="col-md-2">
                                <span class="text-muted">Fecha:</span><br>
                                ${formatearFecha(pedido.fechaCreacion)}
                            </div>
                            <div class="col-md-2">
                                <span class="text-muted">Estado:</span><br>
                                ${traducirEstado(pedido.estadoPedido)}
                            </div>
                            <div class="col-md-3">
                                <span class="text-muted">Enviar a:</span><br>
                                ${pedido.informacionEnvio.nombreCompleto || pedido.informacionPago.nombreTitular}
                            </div>
                            <div class="col-md-2">
                                <span class="text-muted">Productos:</span><br>
                                ${pedido.cantidadProductos}
                            </div>
                            <div class="col-md-3">
                                <span class="text-muted">Total:</span><br>
                                <strong>$${pedido.total.toFixed(2)}</strong>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 text-end">
                        ${botonCancelar}
                        <button class="btn btn-dark btn-detalles" data-pedido='${JSON.stringify(pedido)}'>Ver detalles</button>
                    </div>
                </div>
            </div>
        </div>
    `;
};

const agregarEventListeners = () => {
    document.querySelectorAll('.btn-cancelar').forEach(boton => {
        boton.addEventListener('click', async (e) => {
            const pedidoId = e.target.dataset.pedidoId;
            if (confirm('¿Estás seguro de que deseas cancelar este pedido?')) {
                try {
                    await actualizarEstadoPedido(pedidoId, "CANCELADO");
                    await cargarPedidos();
                } catch (error) {
                    console.error('Error al cancelar el pedido:', error);
                    alert('No se pudo cancelar el pedido. Por favor, intenta nuevamente.');
                }
            }
        });
    });

    document.querySelectorAll('.btn-detalles').forEach(boton => {
        boton.addEventListener('click', (e) => {
            const pedidoData = e.target.dataset.pedido;
            const pedido = JSON.parse(pedidoData);
            verDetallesPedido(pedido);
        });
    });
};

const cargarPedidos = async () => {
    try {
        const contenedor = document.querySelector('.container');
        contenedor.innerHTML = '<p class="text-center">Cargando pedidos...</p>';
        
        const pedidos = await obtenerPedidos();
        const totalPedidos = pedidos.length;
        
        let html = `
            <nav aria-label="breadcrumb" class="mb-4">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">
                        <a href="./profile.html" class="text-decoration-none text-dark">Inicio</a>
                    </li>
                    <li class="breadcrumb-item active">Mis pedidos</li>
                </ol>
            </nav>
            <h1 class="mb-4">Mis pedidos</h1>
            <p class="text-muted">${totalPedidos} pedidos totales</p>
        `;
        
        html += pedidos.map(pedido => crearHTMLPedido(pedido)).join('');
        contenedor.innerHTML = html;
        
        agregarEventListeners();
        
    } catch (error) {
        console.error('Error al cargar los pedidos:', error);
        document.querySelector('.container').innerHTML = `
            <div class="alert alert-danger" role="alert">
                Error al cargar los pedidos. Por favor, intenta nuevamente.
            </div>
        `;
    }
};

const verDetallesPedido = (pedido) => {
    sessionStorage.setItem('pedidoVisualizar', JSON.stringify(pedido));
    window.location.href = '../cliente/order-detail.html';
};

document.addEventListener('DOMContentLoaded', () => {
    const userData = JSON.parse(Cookies.get('userData'));
    if (userData && userData.nombre) {
        document.getElementById('user-name').textContent = userData.nombre;
    }
    cargarPedidos();
});
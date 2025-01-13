import { obtenerPedidos } from "../services/PedidosService.js";
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();
document.addEventListener("DOMContentLoaded", () => {
    const userData = JSON.parse(Cookies.get("userData"));
    if (userData && userData.nombre) {
      document.getElementById("user-name").textContent = userData.nombre;
    }
  });
  
async function loadOrders() {
    try {
        const orders = await obtenerPedidos()
        const ordersContainer = document.getElementById('ordersList');
        
        orders.forEach(order => {
            const orderElement = document.createElement('div');
            orderElement.className = 'order-card';
            orderElement.innerHTML = `
                <div class="order-header">
                    <div class="row align-items-center">
                        <div class="col">
                            <h5 class="mb-0">Pedido #${order.id}</h5>
                        </div>
                        <div class="col text-end">
                            <button class="btn btn-primary manage-order">
                                Gestionar
                            </button>
                        </div>
                    </div>
                </div>
                <div class="order-content">
                    <div class="row">
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Cliente:</strong></p>
                            <p>${order.nombreCliente}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Fecha:</strong></p>
                            <p>${new Date(order.fechaCreacion).toLocaleDateString()}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Estado:</strong></p>
                            <p>${formatStatus(order.estadoPedido)}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Productos:</strong></p>
                            <p>${order.cantidadProductos}</p>
                        </div>
                    </div>
                </div>
            `;
            const manageOrder = orderElement.querySelector('.manage-order');
            manageOrder.addEventListener('click', () => gestionarPedido(order));
            ordersContainer.appendChild(orderElement);
        });
    } catch (error) {
        console.error('Error loading orders:', error);
    }
}

function gestionarPedido(pedido) {
    sessionStorage.setItem('pedidoGestionar', JSON.stringify(pedido));
    window.location.href = '../administrador/order-detail.html';
}

function formatStatus(status) {
    return status.replace(/_/g, ' ').toLowerCase()
        .replace(/\b\w/g, l => l.toUpperCase());
}

document.addEventListener('DOMContentLoaded', loadOrders);
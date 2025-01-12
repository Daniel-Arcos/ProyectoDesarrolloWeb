import { asignarRepartidor, actualizarEstadoPedidoTienda, obtenerPedidoPorId, obtenerProductosPedido } from "../services/PedidosService.js";
import { obtenerRepartidores } from "../services/RepartidoresService.js";
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();

document.addEventListener("DOMContentLoaded", () => {
  const userData = JSON.parse(Cookies.get("userData"));
  if (userData && userData.nombre) {
    document.getElementById("user-name").textContent = userData.nombre;
  }
});

async function loadOrderDetail() {
  try {
    let orderData = JSON.parse(sessionStorage.getItem('pedidoGestionar'));
    orderData = await obtenerPedidoPorId(orderData.id)

    if (!orderData) {
        throw new Error('No hay información del pedido');
    }
    displayOrderInfo(orderData);
    checkOrderStatus(orderData)
    const productsResponse = await obtenerProductosPedido(orderData.id)
    displayProducts(productsResponse);
    const repartidores = await obtenerRepartidores();
    displayDeliveryPeople(repartidores, orderData.idRepartidor)
  } catch (error) {
    console.error("Error loading order details:", error);
  }
}

function displayOrderInfo(order) {
  const shippingInfo = document.getElementById("shippingInfo");
  document.getElementById("orderId").textContent = order.id
  shippingInfo.innerHTML = `
                <p><strong>Dirección:</strong> ${order.informacionEnvio.calle} ${order.informacionEnvio.numeroExterior}</p>
                <p><strong>Colonia:</strong> ${order.informacionEnvio.colonia}</p>
                <p><strong>Ciudad:</strong> ${order.informacionEnvio.ciudad}</p>
                <p><strong>CP:</strong> ${order.informacionEnvio.codigoPostal}</p>
            `;

  const paymentInfo = document.getElementById("paymentInfo");
  paymentInfo.innerHTML = `
                <p><strong>Titular:</strong> ${
                  order.informacionPago.nombreTitular
                }</p>
                <p><strong>Tarjeta:</strong> ****${order.informacionPago.numeroTarjeta.slice(
                  -4
                )}</p>
                <p><strong>Emisor:</strong> ${
                  order.informacionPago.emisorTarjeta
                }</p>
            `;

  const storeOrders = document.getElementById("storeOrders");
  storeOrders.innerHTML = order.pedidosTienda
    .map(
      (store) => `
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">${store.nombreTienda}</h5>
                        <p><strong>Estado:</strong> ${formatStatus(
                          store.estadoPedidoTienda
                        )}</p>
                        <p><strong>Subtotal:</strong> $${store.subtotal}</p>
                        <p><strong>Cantidad de productos:</strong> ${
                          store.cantidadProductos
                        }</p>
                        ${
                          store.estadoPedidoTienda !== "RECIBIDO_EN_ALMACEN"
                            ? `<button class="btn btn-success update-store-state">
                                Confirmar como recibido en almacén
                            </button>`
                            : '<span class="badge bg-success">Recibido en almacén</span>'
                        }
                    </div>
                </div>
            `
    )
    .join("");
    document.querySelectorAll(".update-store-state").forEach((button)=> {
        button.addEventListener('click', () => confirmStoreOrder(order.id))
    })
}

function displayProducts(products) {
  const productsContainer = document.getElementById("products");
  productsContainer.innerHTML = products
    .map(
      (product) => `
                <div class="card mb-3">
                    <div class="row g-0">
                        <div class="col-md-2">
                            <img src="data:image/jpeg;base64,${product.imageData}" 
                                 class="product-image" alt="${product.nombre}">
                        </div>
                        <div class="col-md-10">
                            <div class="card-body">
                                <h5 class="card-title">${product.nombre}</h5>
                                <p class="card-text">${product.descripcion}</p>
                                <p class="card-text">
                                    <small class="text-muted">
                                        Cantidad: ${product.cantidad} | 
                                        Precio unitario: $${product.precioUnitario}
                                    </small>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            `
    )
    .join("");
}

function displayDeliveryPeople(deliveryPeople, idRepartidor) {
  const select = document.getElementById("deliveryPerson");
  deliveryPeople.forEach((person) => {
    const option = document.createElement("option");
    option.value = person.id;
    option.textContent = person.nombreEmpleado;
    select.appendChild(option);
  });
  if (idRepartidor != null) {
    document.getElementById("deliveryPerson").value = idRepartidor;
  }
}

function checkOrderStatus(orderData) {
  if (orderData.estadoPedido === "LISTO_PARA_ENVIO" || orderData.estadoPedido === "EN_REPARTO" 
    || orderData.estadoPedido === "ENTREGADO" || orderData.estadoPedido === "CANCELADO"
  ) {
    const deliverySelect = document.getElementById("deliveryPerson");
    const readyButton = document.getElementById("readyToShip");
    deliverySelect.disabled = true;
    readyButton.disabled = true;
    return;
  }

  const allReceived = orderData.pedidosTienda.every(
    (store) => store.estadoPedidoTienda === "RECIBIDO_EN_ALMACEN"
  );
  
  const deliverySelect = document.getElementById("deliveryPerson");
  const readyButton = document.getElementById("readyToShip");
  
  deliverySelect.disabled = !allReceived;
  readyButton.disabled = !allReceived;
}

async function confirmStoreOrder(storeOrderId) {
  try {
    await actualizarEstadoPedidoTienda(storeOrderId, "RECIBIDO_EN_ALMACEN")
    loadOrderDetail();
  } catch (error) {
    console.error("Error confirming store order:", error);
  }
}

async function markReadyToShip() {
  let orderData = JSON.parse(sessionStorage.getItem('pedidoGestionar'));
  const deliveryPerson = document.getElementById("deliveryPerson").value;
  if (!deliveryPerson) {
    alert("Por favor seleccione un repartidor");
    return;
  }

  try {
    await asignarRepartidor(orderData.id, "LISTO_PARA_ENVIO", deliveryPerson)
    alert("Repartidor asignado correctamente")
    loadOrderDetail()
  } catch (error) {
    console.error("Error marking order as ready to ship:", error);
  }
}

function formatStatus(status) {
  return status
    .replace(/_/g, " ")
    .toLowerCase()
    .replace(/\b\w/g, (l) => l.toUpperCase());
}

document
  .getElementById("readyToShip")
  .addEventListener("click", markReadyToShip);
document.addEventListener("DOMContentLoaded", loadOrderDetail);

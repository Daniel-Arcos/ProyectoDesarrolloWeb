import { obtenerPedidoPorId, obtenerProductosPedido } from "../services/PedidosService.js";

async function loadOrderDetail() {
  try {
    let orderData = JSON.parse(sessionStorage.getItem('pedidoVisualizar'));
    orderData = await obtenerPedidoPorId(orderData.id)

    if (!orderData) {
        throw new Error('No hay información del pedido');
    }
    displayOrderInfo(orderData);
    const productsResponse = await obtenerProductosPedido(orderData.id)
    displayProducts(productsResponse);
  } catch (error) {
    console.error("Error loading order details:", error);
  }
}

function displayOrderInfo(order) {
  document.getElementById('orderId').textContent = order.id
  const shippingInfo = document.getElementById("shippingInfo");
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

function formatStatus(status) {
  return status
    .replace(/_/g, " ")
    .toLowerCase()
    .replace(/\b\w/g, (l) => l.toUpperCase());
}


document.addEventListener("DOMContentLoaded", loadOrderDetail);

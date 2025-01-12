import { actualizarEstadoPedido } from "../services/PedidosService.js";
import { obtenerPedidosRepartidor } from "../services/RepartidoresService.js";
import { cerrarSesion } from "../services/AuthenticationService.js";
const salirButton = document.getElementById("logout");
salirButton.onclick = () => cerrarSesion();

document.addEventListener("DOMContentLoaded", () => {
  const userData = JSON.parse(Cookies.get("userData"));
  if (userData && userData.nombre) {
    document.getElementById("user-name").textContent = userData.nombre;
  }
});

let pedidos = [];
let pedidoSeleccionado = null;
const modal = new bootstrap.Modal(
  document.getElementById("actualizarEstadoModal")
);

async function cargarPedidos() {
  try {
    const userData = JSON.parse(Cookies.get("userData"));
    const idRepartidor = userData.id;
    pedidos = await obtenerPedidosRepartidor(idRepartidor);
    mostrarPedidos(pedidos);
  } catch (error) {
    console.error("Error al cargar pedidos:", error);
  }
}

function mostrarPedidos(pedidosFiltrados) {
  const container = document.getElementById("pedidosContainer");
  container.innerHTML = "";

  pedidosFiltrados.forEach((pedido) => {
    const card = document.createElement("div");
    card.className = "col-md-6 col-lg-4";

    let estadoClass = "";
    switch (pedido.estadoPedido) {
      case "ENTREGADO":
        estadoClass = "bg-success";
        break;
      case "EN_REPARTO":
        estadoClass = "bg-primary";
        break;
      case "LISTO_PARA_ENVIO":
        estadoClass = "bg-warning";
        break;
    }

    card.innerHTML = `
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h5 class="card-title">Pedido #${
                                      pedido.id
                                    }</h5>
                                    <p class="text-muted mb-0">${new Date(
                                      pedido.fechaCreacion
                                    ).toLocaleString()}</p>
                                </div>
                                <span class="badge ${estadoClass}">${pedido.estadoPedido.replace(
      /_/g,
      " "
    )}</span>
                            </div>
                            
                            <div class="mb-3">
                                <h6>Información de envío:</h6>
                                <p class="mb-1">${
                                  pedido.informacionEnvio.nombreCompleto
                                }</p>
                                <p class="mb-1">${
                                  pedido.informacionEnvio.calle
                                } ${pedido.informacionEnvio.numeroExterior}
                                    ${
                                      pedido.informacionEnvio.numeroInterior
                                        ? ", Int. " +
                                          pedido.informacionEnvio.numeroInterior
                                        : ""
                                    }</p>
                                <p class="mb-1">${
                                  pedido.informacionEnvio.colonia
                                }</p>
                                <p class="mb-1">${
                                  pedido.informacionEnvio.ciudad
                                }, CP ${
      pedido.informacionEnvio.codigoPostal
    }</p>
                            </div>
                            
                            ${
                              pedido.estadoPedido !== "ENTREGADO"
                                ? `<button class="btn btn-primary actualizar-estado" data-pedido-id="${pedido.id}">
                                    Actualizar Estado
                                </button>`
                                : ""
                            }
                        </div>
                    </div>
                `;

    container.appendChild(card);
  });

  document.querySelectorAll(".actualizar-estado").forEach((button) => {
    button.addEventListener("click", (e) => {
      pedidoSeleccionado = e.target.dataset.pedidoId;
      modal.show();
    });
  });
}

function filtrarPedidos(e) {
  const filtro = e.target.value;
  const pedidosFiltrados =
    filtro === "todos"
      ? pedidos
      : pedidos.filter((p) => p.estadoPedido === filtro);
  mostrarPedidos(pedidosFiltrados);
}

async function guardarNuevoEstado() {
  if (!pedidoSeleccionado) return;

  const nuevoEstado = document.getElementById("nuevoEstado").value;

  try {
    await actualizarEstadoPedido(pedidoSeleccionado, nuevoEstado);

    await cargarPedidos();
    modal.hide();
  } catch (error) {
    console.error("Error:", error);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  cargarPedidos();

  document
    .getElementById("estadoFilter")
    .addEventListener("change", filtrarPedidos);

  document
    .getElementById("guardarEstado")
    .addEventListener("click", guardarNuevoEstado);
});

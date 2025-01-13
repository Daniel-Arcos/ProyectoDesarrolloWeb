import {
  obtenerTarjetas,
  agregarTarjeta,
  eliminarTarjeta,
} from "../services/ClienteService.js";

document.addEventListener("DOMContentLoaded", () => {
  const userData = JSON.parse(Cookies.get("userData"));
  if (userData && userData.nombre) {
    document.getElementById("user-name").textContent = userData.nombre;
  }
});

document.addEventListener("DOMContentLoaded", async () => {
  try {
    const tarjetas = await obtenerTarjetas();
    console.log(tarjetas);
    tarjetas.forEach((tarjeta) => {
      crearTarjetaTarjeta(tarjeta);
    });
  } catch (error) {
    console.error("Error al cargar tarjetas: ", error);
  }
});

document.getElementById("cardForm").addEventListener("submit", function (e) {
  e.preventDefault();
  const formData = new FormData(this);
  const cardData = Object.fromEntries(formData.entries());
  console.log("Card Data:", cardData);

  const data = agregarTarjeta(cardData);

  crearTarjetaTarjeta(cardData);
  const modal = bootstrap.Modal.getInstance(
    document.getElementById("addCardModal")
  );
  modal.hide();

  this.reset();
});

function crearTarjetaTarjeta(tarjeta) {
  const ultimosDigitos = tarjeta.numeroTarjeta.slice(-4);
  const numeroEnmascarado = `****${ultimosDigitos}`;

  const tarjetaDiv = document.createElement("div");
  tarjetaDiv.dataset.id = tarjeta.id;

  tarjetaDiv.innerHTML = `
         <div class="border rounded p-4 mb-4">
             <div class="d-flex justify-content-between align-items-start mb-3">
                 <div class="d-flex align-items-center">
                     <i class="fas fa-credit-card fa-2x me-2"></i>
                     <span class="h5 mb-0">${tarjeta.emisorTarjeta} ${numeroEnmascarado}</span>
                 </div>
             </div>
             <div class="mb-3">
                 <p class="mb-1">Fecha de caducidad ${tarjeta.mesVencimiento}/${tarjeta.anoVencimiento}</p>
                 <p class="mb-0">${tarjeta.nombreTitular}</p>
             </div>
             <button class="btn btn-outline-danger w-100 delete-card">Eliminar</button>
         </div>
     `;

  const deleteBtn = tarjetaDiv.querySelector(".delete-card");

  deleteBtn.addEventListener("click", () => handleDeleteCard(tarjeta.id));
  document.getElementById("tarjetasContainer").appendChild(tarjetaDiv);
}

document
  .getElementById("addCardModal")
  .addEventListener("hidden.bs.modal", function () {
    document.getElementById("cardForm").reset();
  });

async function handleDeleteCard(id) {
  if (confirm("¿Estás seguro de que deseas eliminar esta tarjeta?")) {
    try {
      await eliminarTarjeta(id);
      const cardCard = document.querySelector(`[data-id="${id}"]`);
      if (cardCard) {
        cardCard.remove();
      }
    } catch (error) {
      console.error("Error al eliminar la tarjeta:", error);
      alert("Hubo un error al eliminar la tarjeta");
    }
  }
}

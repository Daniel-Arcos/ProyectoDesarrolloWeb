import { actualizarCliente } from "../services/ClienteService.js";

document.addEventListener("DOMContentLoaded", () => {
  try {
    const userData = JSON.parse(Cookies.get("userData"));
    cargarDatosUsuario(userData);
  } catch (error) {
    console.error("Error al cargar los datos del usuario:", error);
  }
});

document
  .querySelector("#editPersonalInfoModal .btn-dark")
  .addEventListener("click", async function () {
    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;
    const birthDate = document.getElementById("birthDate").value;

    await actualizarCliente({
      id: JSON.parse(Cookies.get("userData")).id,
      nombre: name,
      telefonoCelular: phone,
      fechaNacimiento: birthDate,
    });
    const userData = JSON.parse(Cookies.get("userData"));
    cargarDatosUsuario(userData);
    const modal = bootstrap.Modal.getInstance(
      document.getElementById("editPersonalInfoModal")
    );
    modal.hide();
  });

function cargarDatosUsuario(userData) {
  if (userData) {
    const userNameElement = document.getElementById("user-name");
    if (userNameElement) {
      userNameElement.textContent = userData.nombre;
    }

    const personalInfoElements = document.querySelectorAll(".personal-info");
    if (personalInfoElements.length > 0) {
      personalInfoElements[0].textContent = userData.nombre;

      personalInfoElements[1].textContent =
        userData.telefonoCelular || "No especificado";

      personalInfoElements[2].textContent = userData.email || "No especificado";

      if (userData.fechaNacimiento) {
        const [year, month, day] = userData.fechaNacimiento.split("-");
        const fechaFormateada = `${day}/${month}/${year}`;
        personalInfoElements[3].textContent = fechaFormateada;
      } else {
        personalInfoElements[3].textContent = "No especificada";
      }
    }

    const nameInput = document.getElementById("name");
    const phoneInput = document.getElementById("phone");
    const birthDateInput = document.getElementById("birthDate");

    if (nameInput) nameInput.value = userData.nombre;
    if (phoneInput) phoneInput.value = userData.telefonoCelular;
    if (birthDateInput && userData.fechaNacimiento) {
      const fecha = new Date(userData.fechaNacimiento);
      birthDateInput.value = fecha.toISOString().split("T")[0];
    }
  }
}

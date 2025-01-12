import { guardarRepartidor, obtenerRepartidores} from '../services/RepartidoresService.js'
import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();
let modal;
        
document.addEventListener("DOMContentLoaded", () => {
    const userData = JSON.parse(Cookies.get("userData"));
    if (userData && userData.nombre) {
      document.getElementById("user-name").textContent = userData.nombre;
    }
  });

async function loadDeliveryPeople() {
    try {
        const deliveryPeople = await obtenerRepartidores();
        const container = document.getElementById('deliverypeopleList');
        container.innerHTML = ''; 
        
        deliveryPeople.forEach(person => {
            const element = document.createElement('div');
            element.className = 'deliverypeople-card';
            element.innerHTML = `
                <div class="deliverypeople-content">
                    <div class="row">
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Nombre:</strong></p>
                            <p>${person.nombreEmpleado}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Email:</strong></p>
                            <p>${person.email}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Fecha de Ingreso:</strong></p>
                            <p>${new Date(person.fechaIngreso).toLocaleDateString()}</p>
                        </div>
                        <div class="col-md-3">
                            <p class="mb-1"><strong>Rol:</strong></p>
                            <p>${formatRole(person.rolEmpleado)}</p>
                        </div>
                    </div>
                </div>
            `;
            container.appendChild(element);
        });
    } catch (error) {
        console.error('Error loading delivery people:', error);
        showAlert('Error al cargar los repartidores', 'danger');
    }
}

function formatRole(role) {
    return role.replace(/_/g, ' ').toLowerCase()
        .replace(/\b\w/g, l => l.toUpperCase());
}

async function submitDeliveryPerson() {
    const nombreEmpleado = document.getElementById('nombreEmpleado').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!nombreEmpleado || !email || !password) {
        showAlert('Por favor complete todos los campos', 'warning');
        return;
    }

    if (password !== confirmPassword) {
        showAlert('Las contraseñas no coinciden', 'warning');
        return;
    }

    if (!validateEmail(email)) {
        showAlert('Por favor ingrese un email válido', 'warning');
        return;
    }

    const newDeliveryPerson = {
        nombreEmpleado,
        email,
        password
    };

    try {
        await guardarRepartidor(newDeliveryPerson);

        modal.hide();
        resetForm();
        loadDeliveryPeople();
        showAlert('Repartidor agregado exitosamente', 'success');
    } catch (error) {
        console.error('Error creating delivery person:', error);
        showAlert('Error al crear el repartidor', 'danger');
    }
}

function validateEmail(email) {
    return String(email)
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
}

function resetForm() {
    document.getElementById('deliveryPersonForm').reset();
}

function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.children[1]);

    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

document.addEventListener('DOMContentLoaded', () => {
    loadDeliveryPeople();
    modal = new bootstrap.Modal(document.getElementById('addDeliveryPersonModal'));
});

document.getElementById("submitDeliveryPerson").addEventListener('click', () => submitDeliveryPerson())
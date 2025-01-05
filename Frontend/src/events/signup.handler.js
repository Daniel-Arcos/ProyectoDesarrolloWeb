import { signup } from '../services/AuthenticationService.js'; 
import { guardarTienda } from '../services/TiendaService.js';

const sellerShopDataDiv = document.getElementById('sellerShopDataDiv');

function userTypesSelectOnChange(selectElement) {
    const selectedValue = selectElement.value;
    if (selectedValue === 'VENDEDOR') {
        sellerShopDataDiv.classList.remove('d-none');
        sellerShopDataDiv.classList.add('d-flex');
    } else if (selectedValue === 'CLIENTE') {
        sellerShopDataDiv.classList.remove('d-flex');
        sellerShopDataDiv.classList.add('d-none');
    }
};

function validateForm(formData) {
    const errors = [];
    
    if (!formData.nombre.trim()) {
        errors.push('El nombre completo es requerido');
    }

    const phoneRegex = /^\d{10}$/;
    if (!phoneRegex.test(formData.telefonoCelular)) {
        errors.push('El número de teléfono debe tener 10 dígitos');
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
        errors.push('El correo electrónico no es válido');
    }

    if (!formData.fechaNacimiento) {
        errors.push('La fecha de nacimiento es requerida');
    }

    if (formData.password.length < 6) {
        errors.push('La contraseña debe tener al menos 6 caracteres');
    }
    if (formData.password !== formData.repeatPassword) {
        errors.push('Las contraseñas no coinciden');
    }

    if (formData.tipoUsuario === 'VENDEDOR') {
        if (!formData.nombreTienda?.trim()) {
            errors.push('El nombre de la tienda es requerido para vendedores');
        }
        if (!formData.descripcionTienda?.trim()) {
            errors.push('La descripción de la tienda es requerida para vendedores');
        }
    }

    return errors;
}

function showErrors(errors) {
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());

    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show';
    alertDiv.setAttribute('role', 'alert');
    
    const errorList = errors.map(error => `<li>${error}</li>`).join('');
    alertDiv.innerHTML = `
        <strong>Por favor, corrige los siguientes errores:</strong>
        <ul>${errorList}</ul>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

    const form = document.getElementById('signupForm');
    form.insertBefore(alertDiv, form.firstChild);
}


function clearErrors() {
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
}

function setupErrorClearingListeners() {
    const inputs = [
        'fullNameInput',
        'phoneNumberInput',
        'emailInput',
        'birthDateInput',
        'passwordInput',
        'repeatPasswordInput',
        'shopNameInput',
        'shopDescriptionInput'
    ];

    inputs.forEach(inputId => {
        const element = document.getElementById(inputId);
        if (element) {
            element.addEventListener('input', clearErrors);
            element.addEventListener('change', clearErrors);
        }
    });

    document.getElementById('choices').addEventListener('change', clearErrors);
}

document.getElementById('choices').addEventListener('change', (event) => userTypesSelectOnChange(event.target));

document.addEventListener('DOMContentLoaded', setupErrorClearingListeners);

document.getElementById('signupForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const formData = {
        tipoUsuario: document.getElementById('choices').value,
        nombre: document.getElementById('fullNameInput').value,
        telefonoCelular: document.getElementById('phoneNumberInput').value,
        email: document.getElementById('emailInput').value,
        fechaNacimiento: document.getElementById('birthDateInput').value,
        password: document.getElementById('passwordInput').value,
        repeatPassword: document.getElementById('repeatPasswordInput').value
    };

    if (formData.tipoUsuario === 'VENDEDOR') {
        formData.nombreTienda = document.getElementById('shopNameInput').value;
        formData.descripcionTienda = document.getElementById('shopDescriptionInput').value;
    }

    const errors = validateForm(formData);
    if (errors.length > 0) {
        showErrors(errors);
        return;
    }
    try {
        const data = await signup(formData);
        if (data.usuario.tipoUsuario === 'CLIENTE') {
            window.location.href = '../views/cliente/dashboard.html';
        } else if (data.usuario.tipoUsuario === 'VENDEDOR') {
            console.log(data)
            const dataTienda = await guardarTienda({
                nombreTienda: formData.nombreTienda,
                descripcion: formData.descripcionTienda,
                idVendedor: data.usuario.id
            })
            window.location.href = '../views/vendedor/dashboard.html';
        }
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error al registrar el usuario';
        console.log(error)
        showErrors([errorMessage]);
    }
});
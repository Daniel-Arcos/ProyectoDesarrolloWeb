import { login } from '../services/AuthenticationService.js'; 

const form = document.getElementById('loginForm')

form.addEventListener('submit', async (event) => { 
    event.preventDefault();
    const email = document.getElementById('floatingInput').value;
    const password = document.getElementById('floatingPassword').value;
    const errorPassword = document.getElementById('errorMessagePassword')
    const errorEmail = document.getElementById('errorMessageEmail')

    if (email === "" || password === "") {
        if (!email) {
            errorEmail.innerHTML = `<label class="text-danger">Campo obligatorio</label>`
        } else {
            errorEmail.innerHTML = "";
        }
        if (!password) {
            errorPassword.innerHTML =`<label class="text-danger">Campo obligatorio</label>`
        } else {
            errorPassword.innerHTML = "";
        }
    } else {
        errorEmail.innerHTML = "";
        errorPassword.innerHTML = "";
        try {
            const data = await login(email, password);
            if (data.usuario.tipoUsuario === 'CLIENTE') {
                window.location.href = '../views/cliente/dashboard.html';
            } else if (data.usuario.tipoUsuario === 'VENDEDOR') {
                window.location.href = '../views/vendedor/dashboard.html';
            }
        } catch (error) {
            console.log("error", error);
            alert('Error al iniciar sesión. Verifica tus credenciales.');
        }
    }
});
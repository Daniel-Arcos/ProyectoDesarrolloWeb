import {cerrarSesion} from '../services/AuthenticationService.js'
const salirButton = document.getElementById('logout');
salirButton.onclick = () => cerrarSesion();

document.addEventListener('DOMContentLoaded', () => {
    const userData = JSON.parse(Cookies.get('userData'));
    if (userData && userData.nombre) {
        document.getElementById('user-name').textContent = userData.nombre;
    } 
});
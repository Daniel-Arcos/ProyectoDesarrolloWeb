import { obtenerTiendaVendedor } from '../services/VendedorService.js'; // Importamos la función de peticiones

document.addEventListener('DOMContentLoaded', async function() {
    try {
        const userData = JSON.parse(Cookies.get('userData'));
        const vendedorId = userData.id;
        const tienda = await obtenerTiendaVendedor(vendedorId);
        console.log('TIenda:', tienda);
        // mostrarProductos(productos);
    } catch (error) {
        console.error('Error al obtener productos:', error);
    }
});
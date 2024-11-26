import { obtenerProductos } from '../services/ProductoService.js'; // Importamos la función de peticiones

const fetchButton = document.getElementById('fetchButton');
const resultContainer = document.getElementById('resultContainer');

// Evento de clic para hacer la petición y mostrar los datos
fetchButton.addEventListener('click', async function() {
    try {
        const data = await obtenerProductos();
        resultContainer.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
    } catch (error) {
        resultContainer.innerHTML = `<p>Error al obtener los datos</p>`;
    }
});

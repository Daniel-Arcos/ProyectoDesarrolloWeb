const baseUrl = 'http://localhost:8085/api/productos';

const obtenerProductos = async () => {
    try {
        const response = await axios.get(baseUrl);
        return response.data;
    } catch (error) {
        console.error('Error al hacer la petición', error);
        throw error;
    }
}

export { obtenerProductos };
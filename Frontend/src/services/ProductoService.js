const baseUrl = 'http://127.0.0.1:8085/api/v1/productos';

const obtenerProductos = async () => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(baseUrl, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error al hacer la petición', error);
        throw error;
    }
}

const guardarProducto = async (datosProducto) => {
    try {
        const token = Cookies.get('Token');
        console.log("Token", token)
        const response = await axios.post(baseUrl, datosProducto, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        return response.data;
    } catch (error) {
        console.error('Error al hacer la petición', error);
        throw error;
    }
}

export { obtenerProductos, guardarProducto };
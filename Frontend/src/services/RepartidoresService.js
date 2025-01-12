const baseUrl = 'http://127.0.0.1:8085/api/v1/empleados/repartidores';

const obtenerRepartidores = async () => {
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

const guardarRepartidor = async (datosRepartidor) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.post(baseUrl, datosRepartidor, {
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

const obtenerPedidosRepartidor = async (idRepartidor) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(`${baseUrl}/${idRepartidor}/pedidos`, {
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

export {guardarRepartidor, obtenerRepartidores, obtenerPedidosRepartidor}
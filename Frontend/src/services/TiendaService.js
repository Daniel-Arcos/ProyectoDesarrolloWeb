const baseUrl = 'http://127.0.0.1:8085/api/v1/tiendas';

const guardarTienda = async (tienda) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.post(baseUrl, tienda, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}

const obtenerProductosTienda = async (idTienda) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(`${baseUrl}/${idTienda}/productos`, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        return response.data;
    } catch (error) {
        throw error;
    }
}

const obtenerPedidosTienda = async (idTienda) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(`${baseUrl}/${idTienda}/pedidos`, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        return response.data;
    } catch (error) {
        throw error;
    }
}

export {guardarTienda, obtenerProductosTienda, obtenerPedidosTienda};
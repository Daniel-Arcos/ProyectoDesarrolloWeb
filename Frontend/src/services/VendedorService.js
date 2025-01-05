const baseUrl = 'http://127.0.0.1:8085/api/v1/vendedores';

const obtenerTiendaVendedor = async (idVendedor) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(`${baseUrl}/${idVendedor}/tienda`, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        return response.data;
    } catch (error) {
        throw error;
    }
}

export {obtenerTiendaVendedor};
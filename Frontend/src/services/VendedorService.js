const baseUrl = 'http://127.0.0.1:8085/api/v1/vendedores';

const obtenerTiendaVendedor = async (idVendedor) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.get(`${baseUrl}/${idVendedor}/tienda`, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        console.log(response)
        Cookies.set('tiendaData', JSON.stringify({
            id: response.data.id,
            nombreTienda: response.data.nombreTienda,
            descripcion: response.data.descripcion,
        }));
        return response.data;
    } catch (error) {
        throw error;
    }
}

export {obtenerTiendaVendedor};
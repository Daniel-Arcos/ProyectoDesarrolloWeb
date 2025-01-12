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

const eliminarProducto = async (idProducto) => {
  try {
    const token = Cookies.get("Token");
    await axios.delete(`${baseUrl}/${idProducto}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const actualizarProducto = async (datosProducto) => {
    try {
        const token = Cookies.get('Token');
        const response = await axios.put(`${baseUrl}/${datosProducto.id}`, datosProducto, {
            headers: {
                'Authorization': `Bearer ${token}` 
            }
        })
        console.log("Producto actualizado", response)
        return response.data;
    } catch (error) {
        console.error('Error al hacer la petición', error);
        throw error;
    }
}

export { obtenerProductos, guardarProducto, actualizarProducto, eliminarProducto };
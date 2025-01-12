const baseUrl = "http://127.0.0.1:8085/api/v1/pedidos";

const obtenerPedidoPorId = async (idPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.get(`${baseUrl}/${idPedido}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const guardarPedido = async (datosPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.post(baseUrl, datosPedido, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const obtenerPedidos = async () => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.get(baseUrl, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const eliminarPedido = async (idPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.delete(`${baseUrl}/${idPedido}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const actualizarEstadoPedidoTienda = async (idPedido, estadoPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.put(
      `${baseUrl}/pedidosTienda/${idPedido}/estado`,
      {
        nuevoEstado: estadoPedido,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const actualizarEstadoPedido = async (idPedido, estadoPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.put(
      `${baseUrl}/${idPedido}/estado`,
      {
        nuevoEstado: estadoPedido,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const asignarRepartidor = async (idPedido, estadoPedido, idRepartidor) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.put(
      `${baseUrl}/${idPedido}/preparar-envio`,
      {
        nuevoEstado: estadoPedido,
        idRepartidor: idRepartidor
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const obtenerProductosPedido = async (idPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.get(`${baseUrl}/${idPedido}/productos`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const obtenerProductosPedidoTienda = async (idPedido) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.get(
      `${baseUrl}/pedidosTienda/${idPedido}/productos`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

export {
  guardarPedido,
  asignarRepartidor,
  actualizarEstadoPedidoTienda,
  obtenerProductosPedido,
  obtenerProductosPedidoTienda,
  eliminarPedido,
  obtenerPedidos,
  obtenerPedidoPorId,
  actualizarEstadoPedido
};

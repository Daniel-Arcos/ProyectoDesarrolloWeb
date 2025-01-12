const baseUrl = "http://127.0.0.1:8085/api/v1/clientes";

const actualizarCliente = async (datosCliente) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.put(
      `${baseUrl}/${datosCliente.id}`,
      datosCliente,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const userDataActual = JSON.parse(Cookies.get("userData"));

    const userDataActualizado = {
      ...userDataActual, 
      nombre: response.data.nombre || userDataActual.nombre,
      telefonoCelular:
        response.data.telefonoCelular || userDataActual.telefonoCelular,
      fechaNacimiento:
        response.data.fechaNacimiento || userDataActual.fechaNacimiento,
    };

    Cookies.set("userData", JSON.stringify(userDataActualizado));

    return response.data;
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const agregarDireccion = async (datosDireccion) => {
  try {
    const token = Cookies.get("Token");
    const userData = JSON.parse(Cookies.get("userData"));
    const clienteId = userData.id;
    const response = await axios.post(
      `${baseUrl}/${clienteId}/direcciones`,
      datosDireccion,
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

const actualizarDireccion = async (datosDireccion) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.put(
      `${baseUrl}/direcciones/${datosDireccion.id}`,
      datosDireccion,
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

const eliminarDireccion = async (idDireccion) => {
  try {
    const token = Cookies.get("Token");
    await axios.delete(`${baseUrl}/direcciones/${idDireccion}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const obtenerDirecciones = async () => {
  try {
    const token = Cookies.get("Token");
    const userData = JSON.parse(Cookies.get("userData"));
    const clienteId = userData.id;
    const response = await axios.get(`${baseUrl}/${clienteId}/direcciones`, {
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

const agregarTarjeta = async (datosTarjeta) => {
  try {
    const token = Cookies.get("Token");
    const userData = JSON.parse(Cookies.get("userData"));
    const clienteId = userData.id;
    const response = await axios.post(
      `${baseUrl}/${clienteId}/tarjetas`,
      datosTarjeta,
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

const eliminarTarjeta = async (idTarjeta) => {
  try {
    const token = Cookies.get("Token");
    await axios.delete(`${baseUrl}/tarjetas/${idTarjeta}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error("Error al hacer la petición", error);
    throw error;
  }
};

const obtenerTarjetas = async () => {
  try {
    const token = Cookies.get("Token");
    const userData = JSON.parse(Cookies.get("userData"));
    const clienteId = userData.id;
    const response = await axios.get(`${baseUrl}/${clienteId}/tarjetas`, {
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
    const userData = JSON.parse(Cookies.get("userData"));
    const clienteId = userData.id;
    const response = await axios.get(`${baseUrl}/${clienteId}/pedidos`, {
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

export {
  actualizarCliente,
  agregarDireccion,
  actualizarDireccion,
  eliminarDireccion,
  obtenerDirecciones,
  agregarTarjeta,
  eliminarTarjeta,
  obtenerTarjetas,
  obtenerPedidos,
};

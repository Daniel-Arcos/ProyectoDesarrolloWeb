const baseUrl = 'http://127.0.0.1:8085/api/v1/empleados'

const obtenerEmpleadoPorId = async (idEmpleado) => {
  try {
    const token = Cookies.get("Token");
    const response = await axios.get(`${baseUrl}/${idEmpleado}`, {
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

export {obtenerEmpleadoPorId}

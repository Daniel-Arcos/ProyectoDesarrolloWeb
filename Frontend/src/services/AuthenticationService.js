const baseUrl = 'http://127.0.0.1:8085/api/v1/auth/';

const login = async (email, password) => {
    try {
        const response = await axios.post(`${baseUrl}authenticate`, {
            email: email,
            password: password
        });
        const token = response.data.Token;
        const refreshToken = response.data.RefreshToken;

        Cookies.set('Token', token, { expires: 1, secure: true });
        Cookies.set('RefreshToken', refreshToken, { expires: 7, secure: true });
        Cookies.set('userData', JSON.stringify({
            id: response.data.usuario.id,
            nombre: response.data.usuario.nombre,
            email: response.data.usuario.email,
            telefonoCelular: response.data.usuario.telefonoCelular,
            fechaNacimiento: response.data.usuario.fechaNacimiento,
            tipoUsuario: response.data.usuario.tipoUsuario
        }));

        return response.data;
    } catch (error) {
        throw error;
    }
}

const signup = async (formData) => {
    try {
        const response = await axios.post(`${baseUrl}register`, formData);
        const token = response.data.Token;
        const refreshToken = response.data.RefreshToken;

        Cookies.set('Token', token, { expires: 1, secure: true });
        Cookies.set('RefreshToken', refreshToken, { expires: 7, secure: true });
        Cookies.set('userData', JSON.stringify({
            id: response.data.usuario.id,
            nombre: response.data.usuario.nombre,
            email: response.data.usuario.email,
            telefonoCelular: response.data.usuario.telefonoCelular,
            fechaNacimiento: response.data.usuario.fechaNacimiento,
            tipoUsuario: data.usuario.tipoUsuario
        }));
        
        return response.data;
    } catch (error) {
        throw error;
    }
}

const cerrarSesion = () => {
    Cookies.remove('userData');
    Cookies.remove('token');
    Cookies.remove('RefreshToken')
    Cookies.remove('tiendaData')
    sessionStorage.clear(); 
    localStorage.clear();

    window.location.href = '../login.html';
}

export { login, signup, cerrarSesion };
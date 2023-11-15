import axios from "axios";
import TokenService from "./token.service";


/**
 * Utilizar axios para realizar peticiones HTTP. La autenticación ya está automáticamente gestionada por él.
 * 
 * Los métodos HTTP se ejecutan de la siguiente manera (para GET como ejemplo):
 * * axios.get(...)
 * * axios.('url', { method: 'GET' })
 */
const instance = axios.create({
    baseURL: "api/v1",
    headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json"
    },
});

instance.interceptors.request.use(
    (config) => {
        const token = TokenService.localAccessToken;
        if (token) {
            config.headers["Authorization"] = 'Bearer ' + token;  // for Spring Boot back-end
        }
        return config;
    }
);

/**
 * Verificar que sigue existiendo una sesión iniciada
 */
instance.interceptors.response.use(
    (res) => {
        return res;
    },
    async (err) => {
        if (err.response.status === 401) {
            const request = await instance.get('/player/me');

            if (request.status === 401) {
                TokenService.removeUser();
            }
        }

        return Promise.reject(err);
    }
);

export default instance;

import axios from "axios";
import TokenService from "./token.service";

const instance = axios.create({
    baseURL: "api/v1",
    headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
    },
});

instance.interceptors.request.use(
    (config) => {
        const token = TokenService.localAccessToken;
        if (token) {
            config.headers["Authorization"] = 'Bearer ' + token;  // for Spring Boot back-end
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

instance.interceptors.response.use(
    (res) => {
        return res;
    },
    async (err) => {
        const originalConfig = err.config;

        if (originalConfig.url !== "/auth/signin" && err.response) {
            // Access Token was expired
            if (err.response.status === 401 && !originalConfig._retry) {
                originalConfig._retry = true;

                try {
                    const rs = await instance.post("/auth/refreshtoken", {
                        refreshToken: TokenService.localRefreshToken,
                    });

                    const { accessToken } = rs.data;
                    TokenService.localAccessToken = accessToken;

                    return instance(originalConfig);
                } catch (_error) {
                    return Promise.reject(_error);
                }
            }
        }

        return Promise.reject(err);
    }
);

export default instance;

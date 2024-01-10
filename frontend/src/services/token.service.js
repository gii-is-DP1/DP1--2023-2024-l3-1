import axios from './api';
import { appStore } from './appStore';

class TokenService {
    get localAccessToken() {
        return this.user?.token;
    }

    get user() {
        return JSON.parse(localStorage.getItem("user"));
    }

    set user(user) {
        const token = {
            token: {
                value: user.token,
                refreshToken: user.refreshToken,
                type: user.type
            }
        };

        delete user.refreshToken;
        delete user.type;

        user = { ...user, ...token };
        appStore.dispatch({ type: 'appStore/setUser', payload: user });
        window.localStorage.setItem("user", JSON.stringify(user));
        this.updateUser();
    }

    updateUser = () => {
        if (this.user && this.localAccessToken) {
            axios.get('player/me').then((response) => {
                const mergedData = { ...this.user, ...response.data };

                appStore.dispatch({ type: 'appStore/setUser', payload: mergedData });
                window.localStorage.setItem("user", JSON.stringify(mergedData));
            });
        }
    }

    removeUser = () => {
        appStore.dispatch({ type: 'appStore/setUser', payload: undefined });
        window.localStorage.removeItem("user");
    }

    /**
     * Rellenar el store de React desde localStorage y recargar la información del usuario
     */
    constructor() {
        appStore.dispatch({ type: 'appStore/setUser', payload: this.user });
        this.updateUser();
    }

}

const tokenService = new TokenService();

export default tokenService;

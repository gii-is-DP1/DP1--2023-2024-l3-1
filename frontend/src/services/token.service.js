import { configureStore, createSlice } from '@reduxjs/toolkit';
import axios from './api';

const tokenSlice = createSlice({
    name: 'tokenStore',
    initialState: {
        user: undefined,
    },
    reducers: {
        setUser: (state, action) => {
            state.user = action.payload;
        }
    }
});

export const tokenStore = configureStore({
    reducer: {
        tokenStore: tokenSlice.reducer
    }
});

class TokenService {
    /**
     * Rellenar el store de React desde localStorage
     */
    constructor() {
        tokenStore.dispatch({ type: 'tokenStore/setUser', payload: this.user });
    }

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
        tokenStore.dispatch({ type: 'tokenStore/setUser', payload: user });
        window.localStorage.setItem("user", JSON.stringify(user));
        this.updateUser();
    }

    updateUser() {
        axios.get('player/me').then((response) => {
            const mergedData = { ...this.user, ...response.data };

            tokenStore.dispatch({ type: 'tokenStore/setUser', payload: mergedData });
            window.localStorage.setItem("user", JSON.stringify(mergedData));
        });
    }

    removeUser() {
        tokenStore.dispatch({ type: 'tokenStore/setUser', payload: undefined });
        window.localStorage.removeItem("user");
    }

}

const tokenService = new TokenService();

export default tokenService;

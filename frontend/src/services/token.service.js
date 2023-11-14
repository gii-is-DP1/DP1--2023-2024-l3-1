import { configureStore, createSlice } from '@reduxjs/toolkit';

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
     * Rellenar el store
     */
    constructor() {
        tokenStore.dispatch({ type: 'tokenStore/setUser', payload: this.user });
    }

    get localAccessToken() {
        return this.user?.token;
    }

    set localAccessToken(token) {
        const user = this.user || {};
        user.token = token;
        this.user = user;
    }

    get localRefreshToken() {
        return this.user?.refreshToken;
    }

    get user() {
        return JSON.parse(localStorage.getItem("user"));
    }

    set user(user) {
        tokenStore.dispatch({ type: 'tokenStore/setUser', payload: user });
        window.localStorage.setItem("user", JSON.stringify(user));
    }

    removeUser() {
        window.localStorage.removeItem("user");
    }

}

const tokenService = new TokenService();

export default tokenService;

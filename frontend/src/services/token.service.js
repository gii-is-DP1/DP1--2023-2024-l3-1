import { createRef } from 'react';

export const localAccessToken = createRef();
export const user = createRef();

class TokenService {
    constructor() {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        const jwt = JSON.parse(localStorage.getItem("jwt"));
        localAccessToken.current = jwt;
        user.current = storedUser;
    }

    get localAccessToken() {
        return localAccessToken.current;
    }

    set localAccessToken(token) {
        window.localStorage.setItem("jwt", JSON.stringify(token));
        localAccessToken.current = token;
    }

    get localRefreshToken() {
        return user?.refreshToken;
    }

    get user() {
        return user.current;
    }

    set user(user) {
        window.localStorage.setItem("user", JSON.stringify(user));
        user.current = user;
    }

    removeUser() {
        window.localStorage.removeItem("user");
        window.localStorage.removeItem("jwt");
        user.current = undefined;
    }

}
const tokenService = new TokenService();

export default tokenService;

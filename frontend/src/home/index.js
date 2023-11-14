import React, { useEffect, useState } from 'react';
import '../static/css/home/home.css';
import tokenService from '../services/token.service';
import DobbleUserListAdmin from '../admin/dobbleUser/DobbleUserListAdmin';
import Login from '../auth/login';
import MainLobby from '../player/main_lobby';

export default function Home(){
    const user = tokenService.user;
    const [component, setComponent] = useState(<></>);

    useEffect(() => {
        if (user) {
            if (user.is_admin) {
                setComponent(<DobbleUserListAdmin />);
            } else {
                setComponent(<MainLobby />);
            }
        } else {
            setComponent(
                <>
                    <img alt="Dobble logo" src="logo.png" width="15%" />
                    <Login />
                </>
            );
        }
    }, [user]);

    return(
        <div className="home-page-container">
            {component}
        </div>
    );
}

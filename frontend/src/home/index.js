import React from 'react';
import '../static/css/home/home.css';
import DobbleUserListAdmin from '../admin/dobbleUser/DobbleUserListAdmin';
import Login from '../auth/login';
import MainLobby from '../player/main_lobby';
import { useSelector } from 'react-redux';

export default function Home(){
    const user = useSelector(state => state.tokenStore.user);

    function getAppropiateComponent() {
        if (user) {
            if (user.is_admin) {
                return (<DobbleUserListAdmin />);
            } else {
                return (<MainLobby />);
            }
        } else {
            return (
                <>
                    <img alt="Dobble logo" src="logo.png" width="15%" />
                    <Login />
                </>
            )
        }
    }

    return(
        <div className="home-page-container">
            {getAppropiateComponent()}
        </div>
    );
}

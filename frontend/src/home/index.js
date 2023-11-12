import React from 'react';
import '../static/css/home/home.css';
import Login from '../auth/login';

export default function Home(){
    return(
        <div className="home-page-container">
            <img alt="Dobble logo" src="logo.png" width="15%" />
            <Login />
        </div>
    );
}

import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import logo from '../static/images/logo_dobble.png';

export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div">
                <h1>Dobble</h1>
                <h3>---</h3>
                <h3>Juega y diviértete</h3> 
                <img src={logo} width={"30%"} height={"30%"}/>
            </div>
        </div>
    );
}

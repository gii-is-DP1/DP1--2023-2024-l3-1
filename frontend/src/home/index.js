import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 

export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div">
                <h1>Dobble Online</h1>
                {/* <h3>---</h3>
                <h3>Find the best vet for your pet</h3>  */}
                <img alt="Dobble logo" src="logo.png" width={"30%"} height={"30%"}/>
            </div>
        </div>
    );
}

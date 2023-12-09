import React from 'react';
import "../../static/css/lobby/playButton.css";
import { Link } from 'react-router-dom';

export default function MainLobby() {
    return (
        <div className="home-page-container">
            <Link to={"/play"}>
                <button className='play-button'>
                    Play!
                </button>
            </Link>
        </div>
    );
}

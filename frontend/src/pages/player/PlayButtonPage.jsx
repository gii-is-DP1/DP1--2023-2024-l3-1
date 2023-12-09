import React from 'react';
import "../../static/css/lobby/playButton.css";
import { Link } from 'react-router-dom';

export default function PlayButonPage() {
    return (
        <div className="home-page-container">
            <Link to={"/play/choose"}>
                <button className='play-button'>
                    Play!
                </button>
            </Link>
        </div>
    );
}

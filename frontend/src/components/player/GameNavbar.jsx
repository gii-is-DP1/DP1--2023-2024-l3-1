import React from 'react';
import { Navbar } from 'reactstrap';

export default function GameNavbar() {

    return (
        <div style={{ position: 'sticky', width: '100vw', zIndex: '1000', top: '0' }}>
            <Navbar style={{ backgroundColor: 'transparent' }}>
                <img alt="Dobble logo" src="/logo.png" style={{
                    height: '100%',
                    width: '100%',
                    maxHeight: 60,
                    maxWidth: 60,
                    padding: '0',
                    marginBottom: '5px' 
                }} />
            </Navbar>
        </div>
    );
}

import React from 'react';
import { useSelector } from 'react-redux';
import { Navbar } from 'reactstrap';

export default function GameNavbar() {
    const navbar = useSelector(state => state.appStore.navbar);

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
                {navbar.content ? <div style={{ display: 'flex', flex: '1', justifyContent: 'center' }}>
                    <h2 style={{ margin: '0 auto' }}>{navbar.content}</h2>
                </div> : undefined}
            </Navbar>
        </div>
    );
}

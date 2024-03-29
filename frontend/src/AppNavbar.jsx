import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router-dom';
import { Collapse, Nav, NavItem, Navbar, NavbarToggler } from 'reactstrap';
import UserAvatar from './components/player/UserAvatar';
import DButton from './components/ui/DButton';
import tokenService from './services/token.service';

export default function AppNavbar() {
    const user = useSelector(state => state.appStore.user);
    const [collapsed, setCollapsed] = useState(true);
    const navigate = useNavigate();
    const location = useLocation();
    const toggleNavbar = () => setCollapsed(!collapsed);

    function getProps(link) {
        const props = {
            onClick: () => {navigate(link)},
            // eslint-disable-next-line react-hooks/rules-of-hooks
            ...(location.pathname === link ? 
                { onClick: undefined, style: { pointerEvents: 'none' } } : { color: 'white' } ),
        };

        return props;
    }

    /**
     * SE UTILIZAN DIFERENTES RUTAS DEPENDIENDO DEL ESTADO DE INICIO DE SESIÓN DEL USUARIO
     */
    /**
     * Rutas que estarán disponibles para administradores
     */
    function adminRoutes() {
        if (user?.is_admin) {
            return (
                <>
                    <NavItem>
                        <DButton {...getProps('/players')}>Jugadores</DButton>
                    </NavItem>
                    <NavItem>
                        <DButton {...getProps('/games')}>Partidas</DButton>
                    </NavItem>
                    <NavItem>
                        <DButton {...getProps('/docs')}>Documentación API</DButton>
                    </NavItem>
                </>
            )
        }
    }

    /**
     * Rutas que estarán disponibles para usuarios autenticados que no son administradores
     */
    function notAdminRoutes() {
        if (user && !user?.is_admin) {
            return (
                <>
                    <NavItem>
                        <DButton {...getProps('/friends')}>Amigos</DButton>
                    </NavItem>
                </>
            )
        }
    }

    /**
     * Rutas que estarán disponibles para usuarios autenticados
     */
    function loggedRoutes() {
        if (user) {
            return (
                <>
                <NavItem className="d-flex">
                    <DButton {...getProps('/achievements')}>Logros</DButton>
                </NavItem>
                <NavItem className="d-flex">
                    <DButton color="red" onClick={tokenService.removeUser}>Cerrar sesión</DButton>
                </NavItem>
                </>
            )
        }
    }

    return (
        <div style={{ position: 'sticky', width: '100vw', zIndex: '1000', top: '0' }}>
            <Navbar expand="md" light style={{ backgroundColor: 'var(--bs-body-bg)'}}>
                {!user?.is_admin ? (
                <DButton {...getProps('/')} style={{ display: 'flex' }}>
                    <img alt="Dobble logo" src="/logo.png" style={
                        { height: '100%', width: '100%', maxHeight: 40, maxWidth: 40, padding: '0', marginBottom: '5px' }
                    } />
                    <p style={{ lineHeight: '2em', marginBottom: '0' }}>Online</p>
                </DButton>
                ) : undefined}

                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {adminRoutes()}
                        {notAdminRoutes()}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar style={{ display: 'flex', alignItems: 'center' }}>
                        {user?.is_admin === false ? (<UserAvatar size="small" user={user} {...getProps('/profile')} />) : undefined}
                        {loggedRoutes()}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

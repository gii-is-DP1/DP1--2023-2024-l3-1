import React, { useState } from 'react';
import { Navbar, NavbarBrand, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import UserAvatar from './components/player/UserAvatar';
import tokenService from './services/token.service';
import DButton from './components/ui/DButton';

export default function AppNavbar() {
    const user = useSelector(state => state.tokenStore.user);
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
                <></>
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
        <div style={{ position: 'absolute', width: '100vw', zIndex: '1000' }}>
            <Navbar expand="md" light color="transparent">
                {!user?.is_admin ? (
                <DButton color="white">
                    <NavbarBrand tag={Link} to="/">
                        <img alt="Dobble logo" src="/logo.png" style={{ height: '100%', width: '100%', maxHeight: 40, maxWidth: 40 }} />
                        <NavbarText style={{ marginLeft: '0.5rem' }}>
                            Online
                        </NavbarText>
                    </NavbarBrand>
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

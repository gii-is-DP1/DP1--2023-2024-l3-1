import React, { useState } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import tokenService from './services/token.service';

export default function AppNavbar() {
    const user = useSelector(state => state.tokenStore.user);
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

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
                        <NavLink style={{ color: "white" }} tag={Link} to="/">Jugadores</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/games">Partidas</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/docs">Documentación API</NavLink>
                    </NavItem>
                </>
            )
        }
    }

    /**
     * Rutas que estarán disponibles para usuarios autenticados que no son administradores
     */
    function notAdminRoutes() {
        if (!user?.is_admin) {
            return (
                    null
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
                <NavItem className="d-flex" tag={Link} to="/achievements">
                    <NavLink style={{ color: "white", cursor: 'pointer' }}>Logros</NavLink>
                </NavItem>
                <NavItem className="d-flex">
                    <NavLink style={{ color: "white", cursor: 'pointer' }} onClick={tokenService.removeUser}>Cerrar sesión</NavLink>
                </NavItem>
                </>
            )
        }
    }

    return (
        <div>
            <Navbar expand="md" dark color="dark">
                {!user?.is_admin ? (<NavbarBrand tag={Link} to="/">
                    <img alt="Dobble logo" src="/logo.png" style={{ height: '100%', width: '100%', maxHeight: 40, maxWidth: 40 }} />
                    <NavbarText style={{ color: "white", marginLeft: '0.5rem' }}>
                        Online
                    </NavbarText>
                </NavbarBrand>) : undefined}

                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {adminRoutes()}
                        {notAdminRoutes()}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {loggedRoutes()}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

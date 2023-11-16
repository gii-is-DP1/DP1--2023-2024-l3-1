import React, { useState } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import tokenService from './services/token.service';

export default function AppNavbar() {
    const user = useSelector(state => state.tokenStore.user);
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    // let adminLinks = <></>;
    // let ownerLinks = <></>;
    // let userLinks = <></>;
    // let userLogout = <></>;
    // let publicLinks = <></>;

    // roles.forEach((role) => {
    //     if (role === "ADMIN") {
    //         adminLinks = (
    //             <>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/owners">Owners</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/pets">Pets</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/vets">Vets</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/consultations">Consultations</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/clinicOwners">Clinic Owners</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/clinics">Clinics</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/users">Users</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/dobbleUsers">DobbleUsers</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Logros</NavLink>
    //                 </NavItem>
    //             </>
    //         )
    //     }
    //     if (role === "OWNER") {
    //         ownerLinks = (
    //             <>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/myPets">My Pets</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/consultations">Consultations</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/plan">Plan</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Logros</NavLink>
    //                 </NavItem>
    //             </>
    //         )
    //     }
    //     if (role === "VET") {
    //         ownerLinks = (
    //             <>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/consultations">Consultations</NavLink>
    //                 </NavItem>
    //             </>
    //         )
    //     }

    //     if (role === "CLINIC_OWNER") {
    //         ownerLinks = (
    //             <>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/clinics">Clinics</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/owners">Owners</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/consultations">Consultations</NavLink>
    //                 </NavItem>
    //                 <NavItem>
    //                     <NavLink style={{ color: "white" }} tag={Link} to="/vets">Vets</NavLink>
    //                 </NavItem>
    //             </>
    //         )
    //     }
    // })

    // if (!jwt) {
    //     publicLinks = (
    //         <>
    //             <NavItem>
    //                 <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
    //             </NavItem>
    //             <NavItem>
    //                 <NavLink style={{ color: "white" }} id="register" tag={Link} to="/register">Registro</NavLink>
    //             </NavItem>
             
    //             <NavItem>
    //                 <NavLink style={{ color: "white" }} id="login" tag={Link} to="/login">Inicio de sesión</NavLink>
    //             </NavItem>
    //         </>
    //     )
    // } else {
    //     userLinks = (
    //         <>
    //             <NavItem>
    //                 <NavLink style={{ color: "white" }} tag={Link} to="/dashboard">Dashboard</NavLink>
    //             </NavItem>
            
    //         </>
    //     )
    //     userLogout = (
    //         <>
    //             <NavItem>
    //                 <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
    //             </NavItem>
    //             <NavItem className="justify-content-end">
    //             <NavLink style={{ color: "white" }} id="profile" tag={Link} to="/profile">{username}</NavLink>
    //             </NavItem>
    //             <NavItem className="d-flex">
    //                 <NavLink style={{ color: "white" }} id="logout" tag={Link} to="/logout">Cerrar sesión</NavLink>
    //             </NavItem>
    //         </>
    //     )

    // }

    /**
     * Se utilizan diferentes rutas dependiendo del estado de inicio de sesión del usuario
     */

    /**
     * Rutas que estarán disponibles para administradores
     */
    function adminRoutes() {
        if (user?.is_admin) {
            return (
                <>
                    <NavItem>
                        <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Documentación API</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Logros</NavLink>
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
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Logros</NavLink>
                    </NavItem>
            )
        }
    }

    /**
     * Rutas que estarán disponibles para usuarios autenticados
     */
    function loggedRoutes() {
        if (user) {
            return (
                <NavItem className="d-flex">
                    <NavLink style={{ color: "white", cursor: 'pointer' }} onClick={tokenService.removeUser}>Cerrar sesión</NavLink>
                </NavItem>
            )
        }
    }

    return (
        <div>
            <Navbar expand="md" dark color="dark">
                <NavbarBrand tag={Link} to="/">
                    <img alt="Dobble logo" src="logo.png" style={{ height: '100%', width: '100%', maxHeight: 40, maxWidth: 40 }} />
                    <NavbarText style={{ color: "white", marginLeft: '0.5rem' }}>
                        Online
                    </NavbarText>
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {notAdminRoutes()}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {adminRoutes()}
                        {loggedRoutes()}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}

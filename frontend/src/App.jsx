import React, { useEffect } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { useSelector } from 'react-redux';
import { Navigate, Route, Routes, useLocation, useNavigate, matchRoutes } from "react-router-dom";
import AppNavbar from "./AppNavbar";
import SignUpForm from "./components/auth/SignUpForm";
import PlayerProfile from "./components/player/PlayerProfile";
import GameListAdminPage from "./pages/admin/GameListAdminPage";
import PlayerListAdminPage from "./pages/admin/PlayerListAdminPage";
import SwaggerDocsPage from "./pages/admin/SwaggerDocsPage";
import AchievementEditAdminPage from "./pages/admin/achievement/AchievementEditAdminPage";
import AchievementListAdminPage from "./pages/admin/achievement/AchievementListAdminPage";
import LoginPage from "./pages/auth/LoginPage";
import SignUpPage from "./pages/auth/SignUpPage";
import AchievementListPlayer from "./pages/player/AchievementListPlayer";
import CreationGamePage from "./pages/player/CreationGamePage";
import GameJoinPage from "./pages/player/GameJoinPage";
import PlayButtonPage from "./pages/player/PlayButtonPage";
import PlayPage from "./pages/player/PlayPage";
import './static/css/home.css';

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      {error.message ? (<pre>{error.message}</pre>) : undefined}
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

function App() {
  const user = useSelector(state => state.tokenStore.user);
  const location = useLocation();
  const navigate = useNavigate();
  
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
          <Route exact path="/" element={<Navigate to="/players" />} />
          <Route exact path="/achievements" element={<AchievementListAdminPage />} />
          <Route exact path="/achievements/new" element={<AchievementEditAdminPage />} />
          <Route exact path="/achievements/edit/:id" element={<AchievementEditAdminPage />} />
          <Route exact path="/players" element={<PlayerListAdminPage />} />
          <Route exact path="/players/new" element={<SignUpForm className="page-container" 
            onSignUp={() => navigate('/')} />} />
          <Route exact path="/players/edit/:id" element={<PlayerProfile />} />
          <Route exact path="/docs" element={<SwaggerDocsPage />} />
          <Route exact path="/games" element={<GameListAdminPage />} />
        </>
      )
    }
  }

  /**
   * Rutas que estarán disponibles para usuarios no autenticados
   */
  function notLoggedRoutes() {
    if (!user) {
      return (
        <>
          <Route exact path="/" element={<LoginPage />} />
          <Route exact path="/signup" element={<SignUpPage />} />
        </>
      );
    }
  }

  /**
   * Rutas que estarán disponibles para usuarios autenticados que no son administradores
   */
  function notAdminRoutes() {
    if (user && !user?.is_admin) {
      return (
        <>
          <Route exact path="/" element={<PlayButtonPage />} />
          <Route exact path="/achievements" element={<AchievementListPlayer />} />
          <Route exact path="/profile" element={<PlayerProfile />} />
          <Route exact path="/play/choose" element={<PlayPage />} />
          <Route exact path="/play/join" element={<GameJoinPage />} />
          <Route exact path="/play/new" element={<CreationGamePage />} />
        </>
      )
    }
  }

  function getRoutes() {
    return (
      <>
        {notLoggedRoutes()}
        {adminRoutes()}
        {notAdminRoutes()}
      </>
    )
  }

  /**
   * LÓGICA DE MIDDLEWARE
   * 
   * Esta lógica de negocio sirve para determinar si el usuario ha visitado una ruta a la que no debe de acceder
   * basándose en sus roles y permisos.
   */
  /**
   * Estas funciones recorren recursivamente todos los elementos JSX buscando el prop path
   */
  const recursivePathFinding = (children) => {
    return children.map((child) => {
      if (!child?.props?.path && Array.isArray(child?.props?.children)) {
        return recursivePathFinding(child.props.children);
      }

      return child?.props?.path;
    })
  };

  function getAvailablePaths() {
    return React.Children
    .map(getRoutes(), (rootElement) => {
      return recursivePathFinding(rootElement.props.children);
    })
    .filter((i) => Boolean(i));
  }

  function getCurrentRoute() {
    const routeObjects = getAvailablePaths().map((p) => {
      return {
        path: p
      }
    });

    const matchedRoutes = matchRoutes(routeObjects, location);

    if (matchedRoutes) {
      const [{ route }] = matchedRoutes;

      return route.path;
    }    
  }

  const isAuthorizedPath = getAvailablePaths().some((p) => {
    return p === getCurrentRoute();
  });

  useEffect(() => {
    if (!isAuthorizedPath && location.pathname !== "/") {
      console.warn('Redirigiendo a / por falta de permisos');
      navigate("/");
    }
  }, [isAuthorizedPath, location]);

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        {user ? <AppNavbar /> : undefined}
        <Routes>
          {getRoutes()}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;

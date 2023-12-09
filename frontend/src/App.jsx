import React, { useEffect } from "react";
import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";
import { useSelector } from 'react-redux';
import AppNavbar from "./AppNavbar";
import SignUpForm from "./components/auth/SignUpForm";
import LoginPage from "./pages/auth/LoginPage";
import AchievementListPlayer from "./pages/player/AchievementListPlayer";
import MainLobby from "./components/player/MainLobby";
import SwaggerDocsPage from "./pages/admin/SwaggerDocsPage";
import AchievementListAdminPage from "./pages/admin/achievement/AchievementListAdminPage";
import AchievementEditAdminPage from "./pages/admin/achievement/AchievementEditAdminPage";
import PlayerListAdminPage from "./pages/admin/PlayerListAdminPage";
import './App.css';
import './static/css/home/home.css';
import PlayerProfile from "./components/player/PlayerProfile";
import PlayPage from "./components/player/PlayPage";
import GameJoinPage from "./pages/player/GameJoinPage";
import CreationGamePage from "./pages/player/CreationGamePage";
import GameListAdminPage from "./pages/admin/GameListAdminPage";
import SignUpPage from "./pages/auth/SignUpPage";

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

function App() {
  const user = useSelector(state => state.tokenStore.user);
  const location = useLocation();
  const navigate = useNavigate();

  /**
   * Middleware
   * 
   * Añadir rutas dependiendo del rol
   */
  const adminLocations = ['player', 'docs', 'achievements', 'games'];
  const notLoggedLocations = ['signup'];

  useEffect(() => {
    const isInAdminPages = adminLocations.filter((route) => location.pathname.includes(route)).length > 0;
    const isInNotLoggedInPages = notLoggedLocations.filter((route) => location.pathname.includes(route)).length > 0;
    if (
      (
        (user?.is_admin && !isInAdminPages) || 
        (!user && !isInNotLoggedInPages) ||
        (user && isInNotLoggedInPages)
      ) &&
      location.pathname !== "/"
    ) {
      console.warn('Redirigiendo a / por falta de permisos');
      navigate("/");
    }
  }, [location, user]);
  
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
          <Route path="/" exact={true} element={<PlayerListAdminPage />} />
          <Route path="/achievements" element={<AchievementListAdminPage />} />
          <Route path="/achievements/new" element={<AchievementEditAdminPage />} />
          <Route path="/achievements/edit/:id" element={<AchievementEditAdminPage />} />
          <Route path="/player/new" element={<SignUpForm onSignUp={() => navigate('/')} />} />
          <Route path="/player/edit/:id" element={<PlayerProfile />} />
          <Route path="/docs" element={<SwaggerDocsPage />} />
          <Route path="/games" element={<GameListAdminPage />} />
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
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/" exact={true} element={<LoginPage />} />
        </>
      );
    }
  }

  /**
   * Rutas que estarán disponibles para usuarios autenticados que no son administradores
   */
  function notAdminRoutes() {
    if (!user?.is_admin) {
      return (
        <>
          <Route path="/achievements" exact={true} element={<AchievementListPlayer />} />
          <Route path="/" exact={true} element={<MainLobby />} />
          <Route path="/profile" exact={true} element={<PlayerProfile />} />
          <Route path="/play" exact={true} element={<PlayPage />} />
          <Route path="/play/join" exact={true} element={<GameJoinPage />} />
          <Route path="/games/creation" exact={true} element={<CreationGamePage />} />

        </>
      )
    }
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        {user ? <AppNavbar /> : undefined}
        <Routes>
          {notLoggedRoutes()}
          {notAdminRoutes()}
          {adminRoutes()}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;

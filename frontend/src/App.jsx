import React, { useEffect } from "react";
import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";
import { useSelector } from 'react-redux';
import AppNavbar from "./AppNavbar";
import Register from "./components/auth/Register";
import Login from "./components/auth/Login";
import AchievementListPlayer from "./components/player/AchievementListPlayer";
import MainLobby from "./components/player/MainLobby";
import SwaggerDocs from "./components/admin/SwaggerDocs";
import AchievementListAdmin from "./components/admin/AchievementListAdmin";
import AchievementEditAdmin from "./components/admin/AchievementEditAdmin";
import PlayerListAdmin from "./components/admin/PlayerListAdmin";
import PlayerEditAdmin from "./components/admin/PlayerEditAdmin";
import './App.css';
import './static/css/home/home.css';
import PlayerProfile from "./components/player/PlayerProfile";
import PlayPage from "./components/player/PlayPage";
import GameJoinPage from "./components/player/GameJoinPage";
import CreationGamePage from "./components/player/CreationGamePage";
import FinishedGameListAdmin from "./components/admin/FinishedGameListAdmin";
import OnGoingGameListAdmin from "./components/admin/OnGoingGameListAdmin";

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
  const notLoggedLocations = ['register'];

  useEffect(() => {
    const isInAdminPages = adminLocations.filter((route) => location.pathname.includes(route)).length > 0;
    const isInNotLoggedInPages = notLoggedLocations.filter((route) => location.pathname.includes(route)).length > 0;
    if (
      (
        (user?.is_admin && !isInAdminPages) || 
        (!user && !isInNotLoggedInPages)
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
          <Route path="/" exact={true} element={<PlayerListAdmin />} />
          <Route path="/achievements" element={<AchievementListAdmin />} />
          <Route path="/achievements/:id" element={<AchievementEditAdmin />} />
          <Route path="/player/edit/:id" element={<PlayerEditAdmin />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          <Route path="/games/finished" element={<FinishedGameListAdmin />} />
          <Route path="/games/ongoing" element={<OnGoingGameListAdmin />} />

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
          <Route path="/register" element={<Register />} />
          <Route path="/" exact={true} element={<Login />} />
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

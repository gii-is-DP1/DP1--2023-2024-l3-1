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

  // let adminRoutes = <></>;
  // let ownerRoutes = <></>;
  // let userRoutes = <></>;
  // let vetRoutes = <></>;
  // let publicRoutes = <></>;
  // let playerRoutes = <></>;

  // roles.forEach((role) => {
  //   if (role === "ADMIN") {
  //     adminRoutes = (
  //       <>
  //         <Route path="/users" exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
  //         <Route path="/users/:username" exact={true} element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
  //         <Route path="/owners" exact={true} element={<PrivateRoute><OwnerListAdmin /></PrivateRoute>} />
  //         <Route path="/owners/:id" exact={true} element={<PrivateRoute><OwnerEditAdmin /></PrivateRoute>} />
  //         <Route path="/clinics" exact={true} element={<PrivateRoute><ClinicListAdmin /></PrivateRoute>} />
  //         <Route path="/clinics/:id" exact={true} element={<PrivateRoute><ClinicEditAdmin /></PrivateRoute>} />
  //         <Route path="/clinicOwners" exact={true} element={<PrivateRoute><ClinicOwnerListAdmin /></PrivateRoute>} />
  //         <Route path="/clinicOwners/:id" exact={true} element={<PrivateRoute><ClinicOwnerEditAdmin /></PrivateRoute>} />
  //         <Route path="/pets" exact={true} element={<PrivateRoute><PetListAdmin /></PrivateRoute>} />
  //         <Route path="/pets/:id" exact={true} element={<PrivateRoute><PetEditAdmin /></PrivateRoute>} />
  //         <Route path="/pets/:petId/visits" exact={true} element={<PrivateRoute><VisitListAdmin /></PrivateRoute>} />
  //         <Route path="/pets/:petId/visits/:visitId" exact={true} element={<PrivateRoute><VisitEditAdmin /></PrivateRoute>} />
  //         <Route path="/vets" exact={true} element={<PrivateRoute><VetListAdmin /></PrivateRoute>} />
  //         <Route path="/vets/:id" exact={true} element={<PrivateRoute><VetEditAdmin /></PrivateRoute>} />
  //         <Route path="/vets/specialties" exact={true} element={<PrivateRoute><SpecialtyListAdmin /></PrivateRoute>} />
  //         <Route path="/vets/specialties/:specialtyId" exact={true} element={<PrivateRoute><SpecialtyEditAdmin /></PrivateRoute>} />
  //         <Route path="/consultations" exact={true} element={<PrivateRoute><ConsultationListAdmin /></PrivateRoute>} />
  //         <Route path="/consultations/:consultationId" exact={true} element={<PrivateRoute><ConsultationEditAdmin /></PrivateRoute>} />
  //         <Route path="/consultations/:consultationId/tickets" exact={true} element={<PrivateRoute><TicketListAdmin /></PrivateRoute>} />
  //         <Route path="/achievements/" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
  //         <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit/></PrivateRoute>} />
  //         <Route path="/dobbleUsers/" exact={true} element={<PrivateRoute><DobbleUserListAdmin /></PrivateRoute>} />
  //         <Route path="/dobbleUsers/:dobbleUserId" exact={true} element={<PrivateRoute><DobbleUserEditAdmin/></PrivateRoute>} />
        
  //       </>)
  //   }
  //   if (role === "OWNER") { /* PLAYER */
  //     ownerRoutes = (
  //       <>
  //         <Route path="/dashboard" element={<PrivateRoute><OwnerDashboard /></PrivateRoute>} />
  //         <Route path="/plan" exact={true} element={<PrivateRoute><PricingPlan /></PrivateRoute>} />
  //         <Route path="/myPets" exact={true} element={<PrivateRoute><OwnerPetList /></PrivateRoute>} />
  //         <Route path="/myPets/:id" exact={true} element={<PrivateRoute><OwnerPetEdit /></PrivateRoute>} />
  //         <Route path="/myPets/:id/visits/:id" exact={true} element={<PrivateRoute><OwnerVisitEdit /></PrivateRoute>} />
  //         <Route path="/consultations" exact={true} element={<PrivateRoute><OwnerConsultationList /></PrivateRoute>} />
  //         <Route path="/consultations/:consultationId" exact={true} element={<PrivateRoute><OwnerConsultationEdit /></PrivateRoute>} />
  //         <Route path="/consultations/:consultationId/tickets" exact={true} element={<PrivateRoute><OwnerConsultationTickets /></PrivateRoute>} />
  //         <Route path="/achievements/" exact={true} element={<PrivateRoute><AchievementListPlayer /></PrivateRoute>} />
  //         <Route path= "/lobby" element={<MainLobby/>}/>
  //       </>)
  //   }
  //   if (role === "VET") {  /* SE ELIMINA */
  //     vetRoutes = (
  //       <>
  //         {/* <Route path="/dashboard" element={<PrivateRoute><OwnerDashboard /></PrivateRoute>} /> */}
  //         <Route path="/myPets" exact={true} element={<PrivateRoute><OwnerPetList /></PrivateRoute>} />
  //         <Route path="/consultations" exact={true} element={<PrivateRoute><VetConsultationList /></PrivateRoute>} />
  //         <Route path="/consultations/:consultationId/tickets" exact={true} element={<PrivateRoute><VetConsultationTickets /></PrivateRoute>} />
  //       </>)
  //   }
  //   if (role === "CLINIC_OWNER") {  /* SE ELIMINA */
  //     vetRoutes = (
  //       <>
  //         <Route path="/owners" exact={true} element={<PrivateRoute><OwnerListClinicOwner /></PrivateRoute>} />
  //         <Route path="/clinics" exact={true} element={<PrivateRoute><ClinicsList /></PrivateRoute>} />
  //         <Route path="/clinics/:id" exact={true} element={<PrivateRoute><EditClinic /></PrivateRoute>} />
  //         <Route path="/consultations" exact={true} element={<PrivateRoute><ConsultationListClinicOwner /></PrivateRoute>} />
  //         <Route path="/consultations/:id" exact={true} element={<PrivateRoute><ConsultationEditClinicOwner /></PrivateRoute>} />
  //         <Route path="/consultations/:id/tickets" exact={true} element={<PrivateRoute><VetConsultationTickets /></PrivateRoute>} />
  //         <Route path="/vets" exact={true} element={<PrivateRoute><VetListClinicOwner /></PrivateRoute>} />
  //         <Route path="/vets/:id" exact={true} element={<PrivateRoute><VetEditClinicOwner /></PrivateRoute>} />
  //       </>)
  //   }
  //   if(role === "PLAYER") {
  //     playerRoutes = (
  //       <>
  //       <Route path="/lobby" exact={true} element={<PrivateRoute><MainLobby /></PrivateRoute>}/>
  //       <Route path="/profile" exaxt element= {<PrivateRoute><Profile/></PrivateRoute>}/>
  //       </>
  //     )
  //   }
  // })
  // if (!user) {
  //   publicRoutes = (
  //     <>        
  //       <Route path="/register" element={<Register />} />
  //       <Route path="/login" element={<Login />} />

  //     </>
  //   )
  // } else {
  //   userRoutes = (
  //     <>
  //       {/* <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} /> */}        
  //       <Route path="/logout" element={<Logout />} />
  //       <Route path="/login" element={<Login />} />
  //       <Route path= "/lobby" element={<MainLobby/>}/>
  //     </>
  //   )
  // }

  /**
   * Middleware
   * 
   * Añadir rutas dependiendo del rol
   */
  const adminLocations = ['player', 'docs', 'achievements'];
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
          <Route path="/achievements/:achievementId" element={<AchievementEditAdmin />} />
          <Route path="/player" element={<PlayerListAdmin />} />
          <Route path="/player/:palyerId" element={<PlayerEditAdmin />} />
          <Route path="/docs" element={<SwaggerDocs />} />
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

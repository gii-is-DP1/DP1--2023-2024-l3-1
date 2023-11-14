import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";

const Logout = () => {
  function sendLogoutRequest() {
    const jwt = tokenService.localAccessToken;
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  return (
    <div className="auth-page-container">
      <div className="auth-form-container">
        <h2 className="text-center text-md">
          Seguro que quieres cerrar la sesión? 
        </h2>
        <div className="options-row">
          <Link className="auth-button" to="/" style={{textDecoration: "none"}}>
            No
          </Link>
          <button className="auth-button" onClick={() => sendLogoutRequest()}>
            Si
          </button>
        </div>
      </div>
    </div>
  );
};

export default Logout;

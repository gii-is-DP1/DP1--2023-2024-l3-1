import React from "react";
import { formStyle } from "../../components/ui/styles/forms";
import { Link } from "react-router-dom";
import SignUpForm from "../../components/auth/SignUpForm";

export default function SignUpPage() {
  return (
    <div>
      <img alt="Dobble logo" src="/logo.png" width="15%" />
      <div style={formStyle}>
        <h1>Regístrate</h1>
        <h3>Es rápido y fácil</h3>
        <SignUpForm setToken />
        <div style={{
          padding: '50px'
        }}>
          <h3>
            ¿Ya tienes una cuenta? <Link to="/">Iniciar sesión</Link>
          </h3>
        </div>
      </div>
    </div>
  );
}

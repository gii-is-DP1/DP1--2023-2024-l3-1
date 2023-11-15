import React, { useState } from "react";
import { Alert } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import DButton from "../../components/DButton";
import DInput from "../../components/DInput";
import { formStyle } from "../../components/styles/forms";
import { Link } from "react-router-dom";

export default function Login() {
  const [message, setMessage] = useState(null)
  const [username, setUsername] = useState(null);
  const [password, setPassword] = useState(null);
  const [email, setEmail] = useState(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit() {
    try {
      setLoading(true);
      setMessage(null);
      const response = await fetch("/api/v1/player/signup", {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify({
          username,
          email,
          password
        }),
      });

      if (response.status === 226) {
        setMessage("Ya existe un jugador registrado con el mismo nombre de usuario");
        return;
      } else if (response.status >= 500) {
        setMessage("Error del servidor");
        return;
      }

      const data = await response.json();
      tokenService.user = data;
      tokenService.localAccessToken = data.token;
    } catch (e) {
      setMessage(String(e));
    } finally {
      setLoading(false);
    }
  }

  async function handleClick(e) {
    e.preventDefault();
    setUsername(username?.trim());
    setPassword(password?.trim());
    setEmail(email?.trim());

    if (username && password && email) {
      await handleSubmit();
    } else {
      setMessage('El nombre de usuario, la contraseña y el e-mail no pueden estar vacíos');
    }
  }

  return (
    <div>
      <img alt="Dobble logo" src="logo.png" width="15%" />
      <div style={formStyle}>
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}

        <h1>Regístrate</h1>
        <h3>Es rápido y fácil</h3>
        <form onSubmit={handleClick} style={formStyle}>
          <DInput type="text" placeholder="Usuario" onChange={(e) => setUsername(e.target.value)} style={{ width: '25vw' }} />
          <DInput type="text" placeholder="Correo electrónico" onChange={(e) => setEmail(e.target.value)} style={{ width: '25vw' }} />
          <DInput type="password" placeholder="Contraseña" onChange={(e) => setPassword(e.target.value)} style={{ width: '25vw' }} />
          <DButton text={loading ? 'Creando cuenta...' : 'Crear cuenta'} style={{ width: '25vw' }} />
        </form>

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

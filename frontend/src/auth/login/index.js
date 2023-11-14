import React, { useState } from "react";
import { Alert } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import DButton from "../../components/DButton";
import DInput from "../../components/DInput";
import { formStyle } from "../../components/sharedStyles";

export default function Login() {
  const [message, setMessage] = useState(null)
  const [username, setUsername] = useState(null);
  const [password, setPassword] = useState(null);  
  const [loading, setLoading] = useState(false);

  async function handleSubmit() {
    try {
      setLoading(true);
      setMessage(null);
      const response = await fetch("/api/v1/player/login", {
        headers: { "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify({
          username,
          password
        }),
      });

      if (response.status === 401) {
        setMessage("Credenciales incorrectas");
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

  async function handleClick (e) {
    e.preventDefault();
    setUsername(username?.trim());
    setPassword(password?.trim());

    if (username && password) {
      await handleSubmit();
    } else {
      setMessage('El nombre de usuario o la contraseña no pueden estar vacíos');
    }
  }
  
    return (
      <div style={formStyle}>
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}

        <h1>Iniciar sesión</h1>
        <form onSubmit={handleClick} style={formStyle}>
          <DInput type="text" placeholder="Usuario" onChange={(e) => setUsername(e.target.value)} style={{ width: '25vw' }} />
          <DInput type="password" placeholder="Contraseña" onChange={(e) => setPassword(e.target.value)} style={{ width: '25vw' }}  />
          <DButton text={loading ? 'Iniciando sesión...' : 'Iniciar sesión' } style={{ width: '25vw' }} />
        </form>

        <div style={{
          padding: '50px'
        }}>
          <h3>
            ¿No tienes una cuenta? <a href="register">Regístrate</a>
          </h3>
        </div>
      </div>
    );  
}

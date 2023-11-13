import React, { useState } from "react";
import { Alert } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";

export default function Login() {
  const [message, setMessage] = useState(null)
  const [username, setUsername] = useState(null);
  const [password, setPassword] = useState(null);  

  async function handleSubmit() {
    setMessage(null);
    const response = await fetch("/api/v1/auth/login", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify({
        username,
        password
      }),
    });

    if (response.status >= 400 && response.status < 500) {
      setMessage("Credenciales incorrectas");
      return;
    } else if (response.status >= 500) {
      setMessage("Error del servidor");
      return;
    }

    const data = await response.json();
    tokenService.setUser(data);
    tokenService.updateLocalAccessToken(data.token);          
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

  const formStyle = {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center'
  }

  const inputStyles = {
    border: 'none',
    outline: 'none',
    backgroundColor: '#F2F9F3',
    borderRadius: '5px',
    padding: '10px',
    margin: '10px',
    width: '25vw',
    fontSize: '1.5rem'
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
          <input type="text" placeholder="Usuario" onChange={(e) => setUsername(e.target.value)} style={inputStyles}/>
          <input type="password" placeholder="Contraseña" onChange={(e) => setPassword(e.target.value)} style={inputStyles} />
          <button style={{
            ...inputStyles,
            backgroundColor: '#61196C',
            color: 'white',
          }}>
            Iniciar sesión
          </button>
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

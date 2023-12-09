import React, { useState } from "react";
import { Alert } from "reactstrap";
import tokenService from "../../services/token.service";
import DButton from "../../components/ui/DButton";
import DInput from "../../components/ui/DInput";
import { formStyle } from "../../components/ui/styles/forms";
import { Link } from "react-router-dom";
import axios from '../../services/api';

export default function LoginPage() {
  const [message, setMessage] = useState(null)
  const [username, setUsername] = useState(null);
  const [password, setPassword] = useState(null);  
  const [loading, setLoading] = useState(false);

  async function handleSubmit() {
    try {
      setLoading(true);
      setMessage(null);
      const response = await axios.post("/player/login", {
          username,
          password
      });

      tokenService.user = response.data;
    } catch (e) {
      if (e.response?.status === 401) {
        setMessage("Credenciales incorrectas");
        return;
      } else if (e.response?.status >= 500) {
        setMessage("Error del servidor");
        return;
      }

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
      <div className="home-page-container">
        <img alt="Dobble logo" src="logo.png" width="15%" />
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
            <DButton style={{ width: '25vw' }} disabled={loading}>
              {loading ? 'Iniciando sesión...' : 'Iniciar sesión' }
            </DButton>
          </form>

          <div style={{
            padding: '50px'
          }}>
            <h3>
              ¿No tienes una cuenta? <Link to="/signup">Regístrate</Link>
            </h3>
          </div>
        </div>
      </div>
    );  
}

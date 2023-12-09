import React, { useState } from "react";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import { formStyle } from "../ui/styles/forms";
import axios from '../../services/api';
import { useModal } from "../../composables/useModal";

/**
 * 
 * @param {*} Props[onSignUp] - Handler when the sign up is successful
 * @param {*} Props[setToken] - If true, the token will be set in the token service 
 * @returns 
 */
export default function SignUpForm(Props) {
  const [message, setMessage] = useState(null)
  const [username, setUsername] = useState(null);
  const [password, setPassword] = useState(null);
  const [email, setEmail] = useState(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit() {
    try {
      setLoading(true);
      setMessage(null);
      const response = await axios.post("/player/signup", {
        username,
        email,
        password
      });

      if (response.status === 226) {
        setMessage("Ya existe un usuario con ese e-mail o nombre de usuario");
        return;
      }

      if (Props.setToken) {
        tokenService.user = response.data;
        tokenService.localAccessToken = response.data.token;
      }

      if (Props.onSignUp) {
        Props.onSignUp();
      }
      
    } catch (e) {
      if (e.response.status === 401) {
        setMessage("Credenciales incorrectas");
        return;
      } else if (e.response.status >= 500) {
        setMessage("Error del servidor");
        return;
      }

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

  const modal = useModal(setMessage, message, 'Error');

  return (
    <>
    {modal}
    <div>
      <form onSubmit={handleClick} style={formStyle}>
        <DInput type="text" placeholder="Usuario" onChange={(e) => setUsername(e.target.value)} style={{ width: '25vw' }} />
        <DInput type="text" placeholder="Correo electrónico" onChange={(e) => setEmail(e.target.value)} style={{ width: '25vw' }} />
        <DInput type="password" placeholder="Contraseña" onChange={(e) => setPassword(e.target.value)} style={{ width: '25vw' }} />
        <DButton style={{ width: '25vw' }}>
          {loading ? 'Creando cuenta...' : 'Crear cuenta'}
        </DButton>
      </form>
    </div>
    </>
  );
}

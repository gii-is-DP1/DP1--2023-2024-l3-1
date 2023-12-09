import React, { useState } from "react";
import tokenService from "../../services/token.service";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import { formStyle } from "../ui/styles/forms";
import axios from '../../services/api';
import { useModal } from "../../composables/useModal";
import { useIconSelector } from "../../composables/useIconSelector";
import { Icon } from "../../models/enums";
import UserAvatar from "../player/UserAvatar";

/**
 * 
 * @param {*} Props[onSignUp] - Handler when the sign up is successful
 * @param {*} Props[setToken] - If true, the token will be set in the token service 
 * @returns 
 */
export default function SignUpForm(Props) {
  const [message, setMessage] = useState(null);
  const [signUpPlayerDto, setSignUpPlayerDto] = useState({
    username: '',
    password: '',
    email: '',
    profile_icon: Icon.MANO_LOGO
  });
  const [confirmPassword, setConfirmPassword] = useState(null);
  const [loading, setLoading] = useState(false);
  const [openGallery, setOpenGallery] = useState(false);

  async function handleSubmit() {
    try {
      setLoading(true);
      setMessage(null);
      const response = await axios.post("/player/signup", signUpPlayerDto);

      if (response.status === 226) {
        setMessage("Ya existe un usuario con ese e-mail o nombre de usuario");
        return;
      }

      if (Props.setToken) {
        tokenService.user = response.data;
      }

      if (Props.onSignUp) {
        Props.onSignUp();
      }
      
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

  async function handleClick(e) {
    e.preventDefault();

    if (signUpPlayerDto.password !== confirmPassword) {
      setMessage('Las contraseñas no coinciden');
      return;
    }

    if (!signUpPlayerDto.username || !signUpPlayerDto.password || !signUpPlayerDto.email) {
      setMessage('El nombre de usuario, la contraseña y el e-mail no pueden estar vacíos');
      return;
    }

    await handleSubmit();
  }

  const modal = useModal(setMessage, message, 'Error');
  const userIconGallery = useIconSelector(signUpPlayerDto, setSignUpPlayerDto, openGallery, setOpenGallery);

  return (
    <>
    {modal}
    {userIconGallery}
    <div {...Props}>
      <form onSubmit={handleClick} style={formStyle}>
        <UserAvatar user={signUpPlayerDto} size="medium" onClick={() => setOpenGallery(true)} />
        <DInput type="text" placeholder="Usuario" style={{ width: '25vw' }}
          onChange={(e) => setSignUpPlayerDto({ ...signUpPlayerDto, username: e.target.value?.trim() })} />
        <DInput type="text" placeholder="Correo electrónico" style={{ width: '25vw' }} 
          onChange={(e) => setSignUpPlayerDto({ ...signUpPlayerDto, email: e.target.value?.trim() })}  />
        <DInput type="password" placeholder="Contraseña" style={{ width: '25vw' }} 
          onChange={(e) => setSignUpPlayerDto({ ...signUpPlayerDto, password: e.target.value?.trim() })}  />
        <DInput type="password" placeholder="Confirmar contraseña" style={{ width: '25vw' }} 
          onChange={(e) => setConfirmPassword(e.target.value)}  />
        <DButton style={{ width: '25vw' }}>
          {loading ? 'Creando cuenta...' : 'Crear cuenta'}
        </DButton>
      </form>
    </div>
    </>
  );
}

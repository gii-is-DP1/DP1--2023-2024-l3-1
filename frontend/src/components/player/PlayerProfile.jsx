import { Alert } from "reactstrap";
import "../../../src/static/css/profile/profilePage.css";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { formStyle } from "../ui/styles/forms";
import axios from '../../services/api';
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import UserAvatar from "./UserAvatar";
import { useIconSelector } from "../../composables/useIconSelector";

export default function PlayerProfile() {
  const [originalUser, setOriginalUser] = useState({});
  const [currentUser, setCurrentUser] = useState({});
  const [message, setMessage] = useState();
  const [editing, setEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [newPassword, setNewPassword] = useState();
  const [confirmPassword, setConfirmPassword] = useState();
  const [openGallery, setOpenGallery] = useState(false);

  const { id } = useParams();

  async function request() {
    try {
      setMessage(null);

      const response = await axios.get(id ? `/player/${id}` : "/player/me");
      const user = {
        email: response.data.email,
        username: response.data.username,
        profile_icon: response.data.profile_icon
      }
      setOriginalUser(user);
      setCurrentUser(user);
    } catch (e) {
      if (e.response.status === 401) {
        setMessage(id ? "El usuario actual no es administrador, no puede editar ni ver otros perfiles" : "Usuario actual no autenticado");
        return;
      } else if (e.response.status >= 500) {
        setMessage("Error del servidor");
        return;
      } else {
        setMessage(String(e));
      }
    }
  }

  async function patchUser() {
    setLoading(true);
    setMessage(null);
    if (newPassword !== confirmPassword) {
      setMessage("Las contraseñas no coinciden");
      return;
    }

    try {
      await axios.patch(id ? `/player/${id}` : "/player/me", currentUser);
      await request();
      setEditing(false);
    } catch (e) {
      if (e.response?.status === 401) {
        setMessage("El usuario actual no es administrador y no puede editar este usuario");
        return;
      } else if (e.response?.status === 404) {
        setMessage("Error en el cliente: no existe el usuario a actualizar");
        return;
      } else if (e.response?.status === 409) {
        setMessage("Ya existe un usuario con ese e-mail o nombre de usuario");
        return;
      } else {
        setMessage(String(e));
        return;
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    const run = async () => {
      await request();
    }
    run();
  }, []);

  /**
   * Restore original user if editing is cancelled
   */
  useEffect(() => {
    if (!editing) {
      setCurrentUser(originalUser);
    }
  }, [editing]);

  const userIconGallery = useIconSelector(currentUser, setCurrentUser, openGallery, setOpenGallery);

  return (
    <>
    {userIconGallery}
    <div className="profile-page-container">
      {message ? (
        <Alert color="primary">{message}</Alert>
      ) : (
        <></>
      )}
      <form style={formStyle}>
        <h1>Mi perfil</h1>

        <h6>Logo:</h6>

        <UserAvatar size="large" user={currentUser} onClick={editing ? () => setOpenGallery(true) : undefined} />

        <div className="profile-field">
          <h6>Nombre de usuario: </h6>
          <DInput type="text" defaultValue={currentUser.username} disabled={!editing} style={{ width: '25vw' }}
            onChange={
              (e) => setCurrentUser({ ...currentUser, username: e.target.value?.trim() })} />
        </div>

        <div className="profile-field">
          <h6>Email: </h6>
          <DInput type="text" defaultValue={currentUser.email} disabled={!editing} style={{ width: '25vw' }}
            onChange={
              (e) => setCurrentUser({ ...currentUser, email: e.target.value?.trim() })} />
        </div>

        {editing ? (
          <div className="profile-field">
            <h6>Contraseña (dejar en blanco para no cambiar): </h6>
            <DInput type="password" placeholder="Nueva contraseña" onChange={(e) => setNewPassword(e.target.value?.trim())} style={{ width: '25vw' }} />
            <DInput type="password" placeholder="Repetir contraseña" onChange={(e) => setConfirmPassword(e.target.value?.trim())} style={{ width: '25vw' }} />
          </div>
        ) : (<></>)}

        <div>
          {editing ? (
            <DButton style={{ width: '25vw', backgroundColor: '#ff3300' }} onClick={(e) => {
              e.preventDefault();
              setEditing(false)
            }}>
              Cancelar
            </DButton>
          ) : (<></>)}
          <DButton style={{ width: '25vw' }} type="submit" onClick={async (e) => {
            e.preventDefault();
            if (editing) {
              await patchUser();
            } else {
              setEditing(!editing);
            }
          }}>
            {editing ?
              loading ? "Guardando..." : "Guardar"
              : "Editar"}
          </DButton>
        </div>
      </form>
    </div>
    </>
  );
}

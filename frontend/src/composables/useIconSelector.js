import { Icon } from "../models/enums";
import UserAvatar from "../components/player/UserAvatar";
import { useModal } from "./useModal";

/**
 * 
 * @param {*} user - El usuario a ser modificado
 * @param {*} setUser - Para actualizar la imagen de perfil del usuario
 * @param {*} setOpen - Para tener control sobre la visibilidad del modal
 * @returns 
 */
export function useIconSelector(user, setUser, open, setOpen) {
  function getModalContents() {
    return open ? (
      <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center' }}>
        <p>En verde, la imagen que está actualmente seleccionada</p>
        {Object.keys(Icon).map((icon) => {
          return (
            <UserAvatar size="medium"
              icon={icon}
              key={icon}
              style={user.profile_icon === icon ? { backgroundColor: 'green' } : undefined}
              onClick={() => {
                setUser({ ...user, profile_icon: icon });
                setOpen();
              }} />
          )
        })}
      </div>
    ) : undefined;
  }

  return useModal(setOpen, getModalContents(), 'Seleccionar imagen de perfil');

}

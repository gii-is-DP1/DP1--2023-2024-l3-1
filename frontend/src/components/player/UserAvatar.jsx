import React from 'react';
import DIcon from '../ui/DIcon';
import { useSelector } from 'react-redux';


/**
 * Por defecto, si no se pasa ningún prop, se mostrará la imagen de perfil del usuario actual. En caso de que el usuario actual
 * no tenga una imagen, se mostrará el logo del dobble por defecto.
 *
 * @param {*} Props[size] - Tamaños posibles: ``x-small`` (30px), ``small`` (50px), ``medium`` (90px), ``large`` (120px)
 * @param {*} Props[user] - Usuario del que se quiere mostrar el avatar (opcional)
 * @param {*} Props[icon] - Icono a mostrar (si no se proporciona usuario)
 * @returns 
 */
export default function UserAvatar(Props) {
    const user = useSelector(state => state.tokenStore.user);
    const getSize = () => {
        switch (Props.size) {
            case "x-small":
                return 30;
            case "small":
                return 50;
            case "medium":
                return 90;
            case "large":
                return 120;
            default:
                return 90;
        }
    }

    return (
        <div {...Props} style={{
            width: `${getSize()}px`,
            height: `${getSize()}px`,
            backgroundColor: 'white',
            borderRadius: '100%',
            padding: '5px',
            margin: '10px',
            ...(Props.onClick && { cursor: 'pointer' }),
            ...Props.style
            }}
            >
            <DIcon icon={Props.user?.profile_icon ?? Props.icon ?? user.profile_icon} style={{
                padding: '2px'
            }} />
        </div>
    );
}

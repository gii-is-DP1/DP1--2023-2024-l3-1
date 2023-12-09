import React from 'react';
import DIcon from '../ui/DIcon';
import { useSelector } from 'react-redux';


/**
 * 
 * @param {*} Props[size] - Tamaños posibles: ``small`` (40px), ``medium`` (80px), ``large`` (120px)
 * @returns 
 */
export default function UserAvatar(Props) {
    const user = useSelector(state => state.tokenStore.user);
    const getSize = () => {
        switch (Props.size) {
            case "small":
                return 40;
            case "medium":
                return 80;
            case "large":
                return 120;
            default:
                return 80;
        }
    }

    return (
        <div style={{
            width: `${getSize()}px`,
            height: `${getSize()}px`,
            backgroundColor: 'white',
            borderRadius: '100%',
            padding: '2%'
            }}>
            <DIcon icon={Props.user?.profile_icon ?? user.profile_icon} style={{
                padding: '2px'
            }} />
        </div>
    );
}

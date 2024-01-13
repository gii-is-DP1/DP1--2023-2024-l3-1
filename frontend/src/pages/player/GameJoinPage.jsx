import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { validate } from 'uuid';
import DButton from "../../components/ui/DButton";
import DInput from "../../components/ui/DInput";
import { useModal } from "../../composables/useModal";
import axios from '../../services/api';

export default function GameJoinPage() {
    const [url, setUrl] = useState("");
    const [message, setMessage] = useState();
    const [header, setHeader] = useState();
    const navigate = useNavigate();

    function handleChange(event) {
        setUrl(event.target?.value?.trim());
    }

    async function joinGame(game_id) {
        try {
            await axios.post(`/games/join/${game_id}`);
        } catch (e) {
            setHeader("Error al unirse a la partida");

            if (e.response?.status === 304) {
                setMessage("Ya estás unido a esta partida");
            } else if (e.response?.status === 404) {
                setMessage("La partida no existe");
            } else if (e.response?.status === 401) {
                setMessage("No estás autorizado para unirte a esta partida");
            } else if (e.response?.status === 423) {
                setMessage("La partida ya ha empezado o ha finalizado");
            } else if (e.response?.status === 509) {
                setMessage("La partida ya está completa");
            } else if (e.response?.status === 500) {
                setMessage("Error del servidor");
            }
        }
    }

    const splittedUrl = (url.split("/") ?? []);
    const maybeUuid = splittedUrl?.[splittedUrl.length - 1];
    const id = validate(maybeUuid) && maybeUuid;
    const modal = useModal(setMessage, message, header);

    return (
        <>
        {modal}
        <div className="page-container">
            <h1>Unirme a la partida</h1>
            <DInput style={{ width: '25vw' }} type="text" name="id" placeholder="Introduce el URL o ID de la partida" onChange={handleChange} />
            <DButton style={{ width: '25vw' }} disabled={!id} onClick={async () => {
                await joinGame(id);
                navigate(`/play/${id}`) 
                }}>{id ? 'Unirme' : 'URL o ID no válido'}</DButton>
        </div>
        </>
    );
}

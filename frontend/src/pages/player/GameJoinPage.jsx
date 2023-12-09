import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { validate } from 'uuid';
import DButton from "../../components/ui/DButton";
import DInput from "../../components/ui/DInput";

export default function GameJoinPage() {
    const [url, setUrl] = useState("");
    const navigate = useNavigate();

    function handleChange(event) {
        setUrl(event.target?.value?.trim());
    }

    const splittedUrl = (url.split("/") ?? []);
    const maybeUuid = splittedUrl?.[splittedUrl.length - 1];
    const id = validate(maybeUuid) && maybeUuid;

    return (
        <div className="page-container">
            <h1>Unirme a la partida</h1>
            <DInput style={{ width: '25vw' }} type="text" name="id" placeholder="Introduce el URL o ID de la partida" onChange={handleChange} />
            <DButton style={{ width: '25vw' }} disabled={!id} onClick={() => navigate(`/play/${id}`)}>{id ? 'Unirme' : 'URL o ID no válido'}</DButton>
        </div>
    );
}

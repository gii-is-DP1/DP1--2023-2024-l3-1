import { useState } from "react";
import tokenService from "../../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchState from "../../util/useFetchState";
import DInput from "../ui/DInput"

const jwt = tokenService.localAccessToken;

export default function PlayerEditAdmin() {
    const id = getIdFromUrl(2);
    const emptyPlayer = {
        id: id === "new" ? null : id,
        username: "",
        email: "",
        password:""
    };
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [player, setPlayer] = useFetchState(
        emptyPlayer,
        `/api/v1/player/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    );
    const modal = getErrorModal(setVisible, visible, message);
    function handleSubmit(event) {
        event.preventDefault();
        fetch(
            "/api/v1/player" + (player.id ? "/" + player.id : ""),
            {
                method: player.id ? "PUT" : "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(player),
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if (data === "")
                    window.location.href = "/player";
                else {
                    let json = JSON.parse(data);
                    if (json.message) {
                        setMessage(JSON.parse(data).message);
                        setVisible(true);
                    } else
                        window.location.href = "/player";
                }
            })
            .catch((message) => alert(message));
    }
    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setPlayer({ ...player, [name]: value });
    }
    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {player.id ? "Editar Jugador" : "Añadir Jugador"}
            </h2>
            <div className="auth-form-container">
                {modal}
                <Form onSubmit={handleSubmit}>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            required
                            name="username"
                            id="username"
                            placeholder="Usuario"
                            value={player.username || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            required
                            name="email"
                            id="email"
                            placeholder="Correo Electrónico"
                            value={player.email || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            name="password"
                            id="password"
                            placeholder="Contraseña"
                            value={player.password || ""}
                            onChange={handleChange}
                        />
                    </div>
                   
                    <div className="custom-button-row">
                        <button className="auth-button">Guardar cambios</button>
                        <Link
                            to={`/player`}
                            className="auth-button-red"
                            style={{ textDecoration: "none" }}
                        >
                            Descartar cambios
                        </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
} 

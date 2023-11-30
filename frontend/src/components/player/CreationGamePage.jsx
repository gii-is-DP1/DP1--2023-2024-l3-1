import { useState } from "react";
import tokenService from "../../services/token.service";
import { Link, useNavigate } from "react-router-dom";
import { Label } from "reactstrap";
import getErrorModal from "../../util/getErrorModal";
import DInput from "../ui/DInput";
import axios from '../../services/api';
import { formStyle } from "../ui/styles/forms";
import DButton from "../ui/DButton";



export default function CreationGamePage() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [game, setGame] = useState({ maxPlayers: 8 });
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setGame({ ...game, [name]: value });
    }

    function handleIncrement() {
        if (game.maxPlayers < 8) {
            setGame(prevGame => ({ ...prevGame, maxPlayers: prevGame.maxPlayers + 1 }));
        }
    }

    function handleDecrement() {
        if (game.maxPlayers > 2) {
            setGame(prevGame => ({ ...prevGame, maxPlayers: prevGame.maxPlayers - 1 }));
        }
    }

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            setLoading(true);
            setMessage(null);
            const response = await axios.post("/games/creation", game);
            setGame(response);
            //TODO: he llamado asi al lobby del game! preguntar
            navigate("/gameLobby")
        } catch (e) {
            if (e.response.status >= 400) {
                setMessage("Error del cliente");
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


    const modal = getErrorModal(setVisible, visible, message);

    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {"Creación de la partida"}
            </h2>
            {modal}
            <div className="auth-form-container">
                <form onSubmit={handleSubmit} style={formStyle}>
                    <div className="custom-form-input">
                        <Label for="name">
                            Nombre de la partida
                        </Label>
                        <DInput
                            type="text"
                            required
                            name="name"
                            id="name"
                            value={game.name}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="custom-form-input">
                        <Label for="maxPlayers">
                            Número máximo de jugadores
                        </Label>
                        <DInput
                            type="number"
                            required
                            name="maxPlayers"
                            id="maxPlayers"
                            value={game.maxPlayers}
                            onChange={handleChange}
                            readOnly // Esto evita la edición directa del input
                            style={{ pointerEvents: "none" }} // Deshabilita eventos de puntero
                        />
                        <DButton type="button" onClick={handleIncrement}>+</DButton>
                        <DButton type="button" onClick={handleDecrement}>-</DButton>
                    </div>
                    <div>
                        <DButton style={{ width: '25vw' }}>
                            {loading ? 'Guardando...' : 'Guardar'}
                        </DButton>
                        <Link
                            to={`/gameLobby`}
                            className="auth-button-red"
                            style={{ textDecoration: "none" }}
                        >
                            <DButton style={{ width: '25vw', backgroundColor: '#ff3300' }}>
                                Cancelar
                            </DButton>
                        </Link>
                    </div>
                </form>
            </div>
        </div>
    );
}
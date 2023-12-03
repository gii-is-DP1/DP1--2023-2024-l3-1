import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import getErrorModal from "../../util/getErrorModal";
import DInput from "../ui/DInput";
import axios from '../../services/api';
import DButton from "../ui/DButton";
import getIdFromUrl from "../../util/getIdFromUrl";

export default function GameLobby() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [game, setGame] = useState({ maxPlayers: 8, start: null });
    const [isCreator, setIsCreator] = useState(false);

    const id = getIdFromUrl(2);

    async function request() {
        try {
            setMessage(null);
            const response = await axios.get(`/games/${id}`);
            const user = await axios.get("/player/me");
            const isCreator = user.data.id === response.data.creator.id;
            if (response.status === 401) {
                setMessage("Partida no existe");
                return;
            } else if (response.status >= 500) {
                setMessage("Error del servidor");
                return;
            }
            setGame(response.data);
            setIsCreator(isCreator);
        } catch (e) {
            setMessage(String(e));
        }
    }

    useEffect(() => {
        const run = async () => {
            await request();
        }

        run();
    }, []);

    const modal = getErrorModal(setVisible, visible, message);

    const handleStartButtonClick = () => {
        if (game.players.length === 1) {
            alert('No hay suficientes jugadores para iniciar la partida');
            // Mostrar un mensaje si solo hay un jugador
        } else {
            const currentDateTime = new Date();
            //Formateo de la fecha
            const year = currentDateTime.getFullYear();
            const month = (currentDateTime.getMonth() + 1).toString().padStart(2, '0');
            const day = currentDateTime.getDate().toString().padStart(2, '0');
            const hours = currentDateTime.getHours().toString().padStart(2, '0');
            const minutes = currentDateTime.getMinutes().toString().padStart(2, '0');
            const seconds = currentDateTime.getSeconds().toString().padStart(2, '0');
            const formattedDateTime = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
            setGame(prevGame => ({ ...prevGame, start: formattedDateTime }));
        }

    };

    return (
        <div style={{ margin: '50px' }}>
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {"Lobby de la partida: " + game.name}
            </h2>
            {modal}
            {isCreator ? (
                <>
                    <h5 style={{ marginTop: '30px' }}>Número máximo de jugadores: {game.maxPlayers}</h5>
                    <h5>Comparte tu url:
                        <DInput type="text" value={game.id} disabled={true} style={{ width: '100%' }} />
                    </h5>
                </>) : undefined}

            <h5>Jugadores Conectados:</h5>
            <ul>
                {Array.isArray(game.players) && game.players.length > 0 ? (
                    game.players.map((player, index) => (
                        <li key={index}>{player.username}</li>
                    ))
                ) : undefined}
            </ul>
            {isCreator ? (
                <>
                    {game && game.players && game.players.length > 1 ? (
                        <Link to={`/play/${id}`}
                            style={{ textDecoration: "none" }}>
                            <DButton
                                style={{ width: '25vw' }}
                                onClick={handleStartButtonClick}
                            >
                                Iniciar Partida
                            </DButton>
                        </Link>
                    ) : (
                        <DButton
                            style={{ width: '25vw' }}
                            onClick={handleStartButtonClick}
                        >
                            Iniciar Partida
                        </DButton>
                    )}
                </>
            ) : undefined}
        </div>
    );
}
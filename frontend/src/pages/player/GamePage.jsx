import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import GameBoard from "../../components/player/GameBoard";
import GameLobby from "../../components/player/GameLobby";
import GameNavbar from "../../components/player/GameNavbar";
import { useModal } from "../../composables/useModal";
import { useRefreshableData } from "../../composables/useRefreshableData";
import { GameStatus } from '../../models/enums';
import axios from '../../services/api';
import { appStore } from "../../services/appStore";

/**
 * Este componente es el principal de la partida. Actualiza el estado cada segundo, pasándoselo a los hijos
 * y renderizando el componente apropiado dependiendo de si la partida está en lobby o ha empezado.
 */
export default function GamePage() {
    const [game, setGame] = useState({});
    const [message, setMessage] = useState();
    const [header, setHeader] = useState();
    const { id } = useParams();
    const navigate = useNavigate();

    async function fetchData() {
        try {
            /**
             * TODO: Actualizar al endpoint correcto si este cambia o es necesario
             */
            const response = await axios.get(`/games/${id}`);
            setGame(response.data);
        } catch (e) {
            /**
             * TODO: Completar todos los tipos de errores que puede devolver el método
             */
            setHeader('Error al actualizar el estado del juego');
            setMessage(String(e));
        }
    }

    async function patchGame() {
        try {
            await axios.patch(`/games/${id}`, game);
        } catch {}
    }

    useEffect(() => {
        if (game.status === GameStatus.FINISHED) {
            setHeader('Partida finalizada');
            setMessage('La partida ya ha finalizado. Cierra esta ventana para volver a la página principal');
        }

        game.status === GameStatus.STARTED ? appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: <GameNavbar />
        }) : appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: undefined
        });
    }, [game.status]);

    useEffect(() => {
        if (!message && game?.status === GameStatus.FINISHED) {
            navigate('/');
        }
    }, [message]);

    useEffect(() => {
        (async () => {
            await patchGame();
        })();
    }, [game.max_players, game.name]);

    useRefreshableData(fetchData, 1);
    const modal = useModal(setMessage, message, header);

    function render() {
        switch (game.status) {
            case GameStatus.LOBBY:
                return (
                    <div className="page-container">
                        <GameLobby game={game} setGame={setGame} />
                    </div>
                );
            case GameStatus.STARTED:
                return (
                    <div className="home-page-container">
                        <div className="page-container">
                            <GameBoard game={game} />
                        </div>
                    </div>
                );
            default:
                return modal;
        }
    }

    return render();
}

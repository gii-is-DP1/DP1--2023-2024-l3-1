import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import GameBoard from "../../components/player/GameBoard";
import GameLobby from "../../components/player/GameLobby";
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
    const user = useSelector(state => state.appStore.user);
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

    async function leaveGame() {
        try {
            await axios.delete(`/games/me`);
        } catch {}
    }

    useEffect(() => {
        if (game.status === GameStatus.FINISHED) {
            setHeader('Partida finalizada');
            setMessage('La partida ya ha finalizado. Cierra esta ventana para volver a la página principal');
        }

        game.status === GameStatus.STARTED ? appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: { name: 'GAME', content: game.name }
        }) : appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: undefined
        });
    }, [game.status, game.name]);

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

    useEffect(() => {
        window.addEventListener('beforeunload', leaveGame);
        return () => {
            window.removeEventListener('beforeunload', leaveGame);
            if (game.status !== GameStatus.FINISHED && user) {
                leaveGame();
            }
            appStore.dispatch({
                type: 'appStore/setNavbar',
                payload: undefined
            });
        }
    }, []);

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

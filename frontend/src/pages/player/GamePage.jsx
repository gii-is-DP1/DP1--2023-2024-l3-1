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
    const isCreator = () => user.username === game.creator.username;
    const [message, setMessage] = useState();
    const [header, setHeader] = useState();
    const [error, setError] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();

    async function fetchData() {
        try {
            const response = await axios.get('/games/me');
            setGame(response.data);
        } catch (e) {
            /**
             * TODO: Completar todos los tipos de errores que puede devolver el método
             */
            setHeader('Error al actualizar el estado del juego');

            if (e.response?.status === 404) {
                setMessage("Ya no estás en esta partida");
            } else if (e.response?.status === 401) {
                setMessage("No estás autenticado o no tienes permisos para esta partida");
            }

            setError(true);
        }
    }

    async function patchGame() {
        if ((game.max_player !== undefined || game.name !== undefined) && isCreator()) {
            try {
                await axios.patch(`/games/${id}`, game);
            } catch {
                setError(true);
            }
        }
    }

    async function leaveGame() {
        try {
            await axios.delete(`/games/me`);
        } catch {
            setError(true);
        }
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
        if (!message && error || (!message && game.status === GameStatus.FINISHED)) {
            navigate('/');
        }
    }, [message, error, game.status]);

    useEffect(() => {
        (async () => {
            await patchGame();
        })();
    }, [game.max_players, game.name]);

    useEffect(() => {
        window.addEventListener('beforeunload', leaveGame);
        return async () => {
            window.removeEventListener('beforeunload', leaveGame);
            if (game.status !== GameStatus.FINISHED && user && game.id) {
                await leaveGame();
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
                        <GameLobby game={game} setGame={setGame} is_creator={isCreator()} setHeader={setHeader} setMessage={setMessage} />
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
            // Mostrar página en blanco mientras se obtiene la información inicial
            default:
                return <div></div>;
        }
    }

    return (
        <>
        {modal}
        {render()}
        </>
    );
}

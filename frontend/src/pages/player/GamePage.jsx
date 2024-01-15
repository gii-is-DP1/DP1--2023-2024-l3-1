import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import { Table } from "reactstrap";
import GameBoard from "../../components/player/GameBoard";
import GameLobby from "../../components/player/GameLobby";
import DButton from "../../components/ui/DButton";
import { useModal } from "../../composables/useModal";
import { useRefreshableData } from "../../composables/useRefreshableData";
import { GameStatus } from '../../models/enums';
import axios from '../../services/api';
import { appStore } from "../../services/appStore";

/**
 * Este componente es el principal de la partida. Actualiza el estado cada segundo, pasándoselo a los hijos
 * y renderizando el componente apropiado dependiendo de si la partida está en lobby o ha empezado.
 */

const tableStyles = {
    maxWidth: '600px',
    width: '100%',
    margin: '0 auto',
};

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
           

            if (e.response?.status === 404) {
                    const responseById = await axios.get(`/games/${id}`);
                    if(responseById.data.status === GameStatus.FINISHED 
                        && responseById.data.game_players.some(gp => gp.username === user.username)){
                        setGame(responseById.data);
                        return;
                     }
                    setHeader('Error al obtener los datos de la partida');
                    setMessage("No estás autenticado o no tienes permisos para esta partida");
                    setError(true);
            } else if (e.response?.status === 401) {
                setHeader('Error al actualizar el estado del juego');
                setMessage("No estás autenticado o no tienes permisos para esta partida");
                setError(true);
            }

            
        }
    }

    function renderTable() {
        if (game.status !== GameStatus.FINISHED) {
            return null;
        }

        const gamePlayers = game.game_players;
        const userPosition = gamePlayers.findIndex(player => player.username === user.username);

        return (
            <div>
                <h2>Puestos de la partida:</h2>
                <Table aria-label="game-positions" className="mt-4" style={tableStyles}>
                <thead>
                    <tr>
                        <th className="text-center">Puesto</th>
                        <th className="text-center">Jugador</th>
                    </tr>
                </thead>
                <tbody>
                    {gamePlayers.map((player, index) => (
                        <tr key={player.id} style={userPosition === index ? {backgroundColor: 'rgb(114, 0, 112)'} : null}>
                            <td className="text-center">{index + 1}</td>
                            <td className="text-center">{player.username}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
                {userPosition === 0 && (
                    <p>Felicidades, ¡has ganado la partida!</p>
                )}
            </div>
        );
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
            if (game.status !== GameStatus.FINISHED && user && game.id) 
                await axios.delete(`/games/me`);
            
        } catch {
            setError(true);
        }
    }

    useEffect(() => {
        game.status === GameStatus.STARTED ? appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: { name: 'GAME', content: game.name }
        }) : appStore.dispatch({
            type: 'appStore/setNavbar',
            payload: undefined
        });
    }, [game.status, game.name]);

    useEffect(() => {
        if (!message && error) {
            navigate('/');
        }
    }, [message, error]);

    useEffect(() => {
        (async () => {
            await patchGame();
        })();
    }, [game.max_players, game.name]);

    useEffect(() => {
        window.addEventListener('beforeunload', leaveGame);
        return async () => {
            window.removeEventListener('beforeunload', leaveGame);
            
            await leaveGame();
            
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
                    <GameBoard game={game} />
                );
            case GameStatus.FINISHED:
                return (
                    <div style={{ textAlign: 'center', marginTop: '50px' }}>
                        <p>El juego ha terminado.</p>
                        {renderTable()}
                        <DButton onClick={() => {
                            navigate(`/`)
                        }}>
                            Volver al menú
                        </DButton>
                        <DButton onClick={() => {
                            navigate(`/play/new`)
                        }}>
                            Nueva partida
                        </DButton>
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

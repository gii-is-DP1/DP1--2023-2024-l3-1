import { Table } from "reactstrap";
import { useState, useEffect } from "react";
import { GameStatus } from "../../models/enums";
import getModal from "../../util/getModal";
import axios from '../../services/api';

export default function FinishedGameListAdmin() {
    const [message, setMessage] = useState();
    const [refreshCounter, setRefreshCounter] = useState(5);
    const [games, setGames] = useState([]);

    async function fetchData() {
        try {
            const response = await axios.get('/games');
            if (response.status === 204) {
                setMessage("No hay partidas finalizadas que mostrar");
            }
            setGames(response.data);
        } catch (error) {
            if (error.response.status === 401) {
                setMessage("El usuario actual no es administrador o no ha iniciado sesión: no tiene permisos para obtener partidas")
            } else if (error.response.status >= 500) {
                setMessage("Error interno del servidor");
            } else {
                setMessage(String(error));
            }
        } finally {
            setRefreshCounter(5);
        }
    };

    useEffect(() => {
        fetchData();

        const timer = setInterval(() => {
            if (refreshCounter > 0) {
                setRefreshCounter(prevRefreshCounter => prevRefreshCounter - 1);
            }
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, []);

    useEffect(() => {
        if (refreshCounter === 0) {
            fetchData();
        }
    }, [refreshCounter]);

    function getStatus(game) {
        // eslint-disable-next-line default-case
        switch (game.status) {
            case GameStatus.FINISHED: {
                return 'Finalizada';
            }
            case GameStatus.LOBBY: {
                return 'En lobby';
            }
            case GameStatus.STARTED: {
                return 'Empezada';
            }
        }
    }

    const finishedGameList =
        games.map((game) => {
            return (
                <tr key={game.id}>
                    <td className="text-center">{game.name}</td>
                    <td className="text-center">{getStatus(game)}</td>
                    <td className="text-center"> {game.creator.username} </td>
                    <td className="text-center">
                        {Array.from(game.players).map((player, index, array) => (
                            <span key={player.id}>
                                {player.username}
                                {index < array.length - 1 && ', '}
                            </span>
                        ))}
                    </td>
                    <td className="text-center">
                        {game.start ? new Date(game.start).toLocaleString('es-ES', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        }) : '-'}
                    </td>
                    <td className="text-center">
                        {game.finish ? new Date(game.finish).toLocaleString('es-ES', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        }) : '-'}
                    </td>
                </tr>)
        });

    const modal = getModal(setMessage, message, 'Error');

    return (
        <>
            {modal}
            <div>
                <div className="admin-page-container">
                    <h1 className="text-center" style={{ marginTop: '30px' }}>Partidas</h1>
                    <p style={{ display: 'flex', justifyContent: 'flex-end' }}>
                        La información se actualiza automáticamente cada 5 segundos. Próxima actualización en {refreshCounter} segundos...
                    </p>
                    <div>
                        <Table aria-label="games" className="mt-4">
                            <thead>
                                <tr>
                                    <th className="text-center">Nombre Partida</th>
                                    <th className="text-center">Estado</th>
                                    <th className="text-center">Creador</th>
                                    <th className="text-center">Jugadores</th>
                                    <th className="text-center">Fecha de creación</th>
                                    <th className="text-center">Fecha de finalización</th>
                                </tr>
                            </thead>
                            <tbody style={{ width: '100%' }}>
                                {finishedGameList}
                            </tbody>
                        </Table>
                    </div>
                </div>
            </div>
        </>
    );
}

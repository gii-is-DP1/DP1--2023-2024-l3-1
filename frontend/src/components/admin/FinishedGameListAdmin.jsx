import { Button, Table } from "reactstrap";
import { useState, useEffect } from "react";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import { Link } from "react-router-dom";
import axios from '../../services/api';
import DButton from "../ui/DButton";

export default function FinishedGameListAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [games, setGames] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/games');
                setGames(response.data);
            } catch (error) {
                console.error('Error al obtener las partidas:', error);
            }
        };
        fetchData();
    }, []);

    const finishedGameList =
        games.map((game) => {
            return (
                game.finished ? (
                    <tr key={game.id}>
                        <td className="text-center" style={{ verticalAlign: 'middle' }}>{game.name}</td>
                        <td className="text-center" style={{ verticalAlign: 'middle' }}> {game.creator.username} </td>
                        <td className="text-center" style={{ verticalAlign: 'middle' }}>
                            {Array.from(game.players).map((player, index, array) => (
                                <span key={player.id}>
                                    {player.username}
                                    {index < array.length - 1 && ', '}
                                </span>
                            ))}
                        </td>
                        <td className="text-center" style={{ verticalAlign: 'middle' }}> {new Date(game.start).toLocaleString('es-ES', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        })} </td>
                        <td className="text-center" style={{ verticalAlign: 'middle' }}> {new Date(game.finish).toLocaleString('es-ES', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                        })} </td>
                        {console.log(game.isFinished)}
                    </tr>) : undefined
            )
        });
    const modal = getErrorModal(setVisible, visible, message);
    return (
        <div>
            <div className="admin-page-container">
                <h1 className="text-center" style={{ marginTop: '30px' }}>Listado Partidas Jugadas</h1>
                {alerts.map((a) => a.alert)}
                {modal}
                <div>
                    <Table aria-label="games" className="mt-4">
                        <thead>
                            <tr>
                                <th className="text-center">Nombre Partida</th>
                                <th className="text-center">Creador</th>
                                <th className="text-center">Jugadores</th>
                                <th className="text-center">Fecha de creación</th>
                                <th className="text-center">Fecha de finalización</th>
                                <th className="text-center"></th>
                            </tr>
                        </thead>
                        <tbody>{finishedGameList}</tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}



import { Button, Table } from "reactstrap";
import { useState } from "react";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import { Link } from "react-router-dom";

const jwt = tokenService.localAccessToken;

export default function PlayerListAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [players, setPlayers] = useFetchState(
        [],
        `/api/v1/player`,
        jwt
    );
    const playerList =
        players.map((p) => {
            return (
                <tr key={p.id}>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>{p.profile_icon}</td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {p.username} </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {p.email} </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {p.is_admin ? "ADMIN" : "PLAYER" } </td>
                    <td className="text-center">
                        <Link to={`/player/${p.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
                            <button className="auth-button-yellow">
                                Editar
                            </button>
                        </Link>
                    </td>
                    <td className="text-center">
                        <button
                            className="auth-button-red"
                            onClick={() =>
                                deleteFromList(
                                    `/api/v1/player/${p.id}`,
                                    p.id,
                                    [players, setPlayers],
                                    [alerts, setAlerts],
                                    setMessage,
                                    setVisible
                                )
                            }
                        >
                            Borrar
                        </button>
                    </td>
                </tr>
            );
        });
    const modal = getErrorModal(setVisible, visible, message);
    return (
        <div>
            <div className="admin-page-container">
                <h1 className="text-center" style={{ marginTop: '30px' }}>Listado Jugadores</h1>
                {alerts.map((a) => a.alert)}
                {modal}
                <div>
                    <Table aria-label="player" className="mt-4">
                        <thead>
                            <tr>
                                <th className="text-center">Icono</th>
                                <th className="text-center">Usuario</th>
                                <th className="text-center">Correo Electrónico</th>
                                <th className="text-center">Rol</th>
                                <th className="text-center">Acciones</th>
                                <th className="text-center"></th>
                            </tr>
                        </thead>
                        <tbody>{playerList}</tbody>
                    </Table>
                    <div className="custom-button-row">
                        <Link 
                            to={`/player/new`}
                            style={{ textDecoration: "none" }}
                        > 
                            <button className="auth-button"> Crear Jugador </button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

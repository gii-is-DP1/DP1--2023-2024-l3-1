import { Table } from "reactstrap";
import { useState, useEffect } from "react";
import { useModal } from "../../composables/useModal";
import { Link } from "react-router-dom";
import { usePaginationButtons } from "../../composables/usePaginationButtons";
import { useRefreshableData } from "../../composables/useRefreshableData";
import { dividirArray } from "../../util/dataManipulation";
import axios from '../../services/api';
import DButton from "../../components/ui/DButton";
import UserAvatar from "../../components/player/UserAvatar";

export default function PlayerListAdmin() {
    const [message, setMessage] = useState();
    const [modalHeader, setModalHeader] = useState();
    const [modalActions, setModalActions] = useState();
    const [currentPage, setCurrentPage] = useState(0);
    const [players, setPlayers] = useState([[]]);

    async function fetchData() {
        try {
            const response = await axios.get('/player');
            // Para probar la paginación, cambiar este número
            setPlayers(dividirArray(response.data, 10));
        } catch (error) {
            setMessage("Error al obtener jugadores.");
        }
    }

    async function deleteUser(id) {
        try {
            await axios.delete(`/player/${id}`);
        } catch (e) {
            setModalHeader("Error al borrar jugador");
            if (e.response?.status === 401) {
                setMessage("El usuario actual no es administrador, no tiene permisos para borrar este jugador.");
            } else if (e.response?.status === 404) {
                setMessage("El jugador no existe.");
            } else if (e.response?.status === 500) {
                setMessage("Error interno del servidor.");
            } else {
                setMessage(String(e));
            }
        } finally {
            await fetchData();
        }
    }

    function setConfirmActions(id) {
        const clear = () => {
            setMessage();
            setModalActions();
            setModalHeader();
        };
        setModalActions(
        <>
            <DButton style={{ backgroundColor: 'red' }} onClick={() => clear()}>
                Cancelar
            </DButton>
            <DButton onClick={() => {
                deleteUser(id);
                clear();
                }}>
                    Confirmar
            </DButton>
        </>);
        setModalHeader("Va a eliminar a un jugador");
        setMessage("¿Continuar?");
    }

    useEffect(() => {
        fetchData();
    }, []);

    const playerList =
        players[currentPage].map((p) => {
            return (
                <tr key={p.id}>
                    <td className="text-center">
                        <UserAvatar user={p} size="small" />
                    </td>
                    <td className="text-center">
                        {p.username}
                    </td>
                    <td className="text-center">
                        {p.email}
                    </td>
                    <td className="text-center">
                        {p.is_admin ? "Administrador" : "Jugador"}
                    </td>
                    <td className="text-center">
                        <Link to={`/player/edit/${p.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
                            <DButton style={{ width: '15vw', backgroundColor: '#ffcc24', color: 'black' }}>
                                Ver Perfil
                            </DButton>
                        </Link>
                    </td>
                    <td className="text-center">
                        <DButton style={{ width: '15vw', backgroundColor: '#ff3300', color: 'black' }}
                            onClick={() => setConfirmActions(p.id)}>
                            Borrar
                        </DButton>
                    </td>
                </tr>
            );
        });

    const modal = useModal(setMessage, message, modalHeader, modalActions);
    const paginationButtons = usePaginationButtons(setCurrentPage, currentPage, players);
    const refreshInfo = useRefreshableData(fetchData, 5);

    return (
        <>
            {modal}
            <div>
            <div className="admin-page-container">
                <h1 className="text-center" style={{ marginTop: '30px' }}>Jugadores</h1>
                <div style={{ display: 'flex', justifyContent: 'flex-end', marginRight: '10px' }}>
                    {refreshInfo}
                </div>
                <div>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                    }}>
                        <Link
                            to={`/player/new`}
                            style={{ textDecoration: "none" }}
                        >
                            <DButton style={{ width: '25vw' }}>Crear Jugador</DButton>
                        </Link>
                    </div>
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
                        <tbody>
                            {playerList}
                        </tbody>
                    </Table>
                    <p>La información se actualiza automáticamente cada 5 segundos</p>
                    {paginationButtons}
                </div>
            </div>
        </div>
        </>
    );
}

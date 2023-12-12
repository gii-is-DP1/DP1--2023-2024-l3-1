import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Link, useParams } from "react-router-dom";
import { Table } from "reactstrap";
import { useModal } from "../../composables/useModal";
import { usePaginationButtons } from "../../composables/usePaginationButtons";
import { useRefreshableData } from "../../composables/useRefreshableData";
import axios from '../../services/api';
import { dividirArray } from "../../util/dataManipulation";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import UserAvatar from "./UserAvatar";

export default function FriendsList() {
    const [message, setMessage] = useState();
    const [modalHeader, setModalHeader] = useState();
    const [modalActions, setModalActions] = useState();
    const [currentPage, setCurrentPage] = useState(0);
    const [friends, setFriends] = useState([[]]);
    const [currentUser, setCurrentUser] = useState({});
    const user = useSelector(state => state.tokenStore.user);
    /**
     * Por alguna razón tarda demasiado en actualizar esta variable al hacerlo de forma asíncrona
     * por lo que se utiliza una variable normal
     */
    let newFriendUsername;

    const { id } = useParams();
    const getCurrentUser = () => {
        return id ? currentUser : user;
    }

    async function fetchData() {
        try {
            const response = await axios.get(id ? `/player/friends/${id}` : '/player/friends');

            if (id) {
                const userRequest = await axios.get(`/player/${id}`);

                setCurrentUser(userRequest.data);
            }
            // Para probar la paginación, cambiar este número
            setFriends(dividirArray(response.data, 10));
        } catch (error) {
            setMessage("Error al obtener amigos");
        }
    }

    /**
     * @param {*} friend_id - Cuando el usuario no es administrador, friend_id debe ser el nombre del amigo
     */
    async function deleteFriend(friend_username) {
        try {
            await axios.delete(id ? `/player/friends/${id}/${friend_username}` : `/player/friends/${friend_username}`);
        } catch (e) {
            setModalHeader("Error al borrar amigo");
            if (e.response?.status === 401) {
                setMessage("El usuario actual no es administrador, no tiene permisos para borrar este amigo.");
            } else if (e.response?.status === 404) {
                setMessage("El amigo no existe");
            } else if (e.response?.status === 500) {
                setMessage("Error interno del servidor.");
            } else {
                setMessage(String(e));
            }
        } finally {
            await fetchData();
        }
    }

    async function addFriend(friend_username) {
        try {
            await axios.put(id ? `/player/friends/${id}/${friend_username}` : `/player/friends/${friend_username}`);
        } catch (e) {
            setModalHeader("Error al borrar amigo");
            if (e.response?.status === 304) {
                setMessage("Ambos usuarios ya son amigos");
            } else if (e.response?.status === 401) {
                setMessage("El usuario actual no es administrador, no tiene permisos para borrar este amigo.");
            } else if (e.response?.status === 404) {
                setMessage("El amigo no existe");
            } else if (e.response?.status === 500) {
                setMessage("Error interno del servidor.");
            } else {
                setMessage(String(e));
            }
        } finally {
            await fetchData();
        }
    }

    function setDeleteConfirmActions(username) {
        const clear = () => {
            setMessage();
            setModalActions();
            setModalHeader();
        };
        setModalActions(
            <>
                <DButton color="red" onClick={() => clear()}>
                    Cancelar
                </DButton>
                <DButton color="green" onClick={() => {
                    deleteFriend(username);
                    clear();
                }}>
                    Confirmar
                </DButton>
            </>);
        setModalHeader("Va a eliminar a un amigo");
        setMessage("¿Continuar?");
    }

    function setAddFriendModal() {
        const clear = () => {
            setMessage();
            setModalActions();
            setModalHeader();
        };
        setModalActions(
            <>
                <DButton color="red" onClick={() => clear()}>
                    Cancelar
                </DButton>
                <DButton color="green" onClick={() => {
                    addFriend(newFriendUsername);
                    clear();
                }}>
                    Confirmar
                </DButton>
            </>);
        setModalHeader("Añadir amigo");
        setMessage(
            <>
                Introduzca el nombre del amigo a añadir:
                <DInput type="text" onChange={(e) => newFriendUsername = e.target.value?.trim()} />
            </>
        );
    }

    useEffect(() => {
        fetchData();
    }, []);

    const friendsList =
        friends?.[currentPage]?.map((p) => {
            return (
                <tr key={p.username} style={{ verticalAlign: 'middle' }}>
                    <td className="text-center">
                        <UserAvatar user={p} size="small" />
                    </td>
                    {p?.username ? <td className="text-center">{p.username}</td> : undefined}
                    {p?.email ? <td className="text-center">{p.email}</td> : undefined}
                    {user.is_admin ? <td className="text-center">{p?.is_admin ? "Administrador" : "Jugador"}</td> : undefined}
                    {user.is_admin ? <td className="text-center">
                        <Link to={`/players/edit/${p.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
                            <DButton color="yellow" style={{ width: '15vw' }}>
                                Ver Perfil
                            </DButton>
                        </Link>
                    </td> : undefined}
                    <td className="text-center">
                        <DButton color="red" style={{ width: '15vw' }}
                            onClick={() => setDeleteConfirmActions(p.username)}>
                            Borrar
                        </DButton>
                    </td>
                </tr>
            );
        });

    const modal = useModal(setMessage, message, modalHeader, modalActions);
    const paginationButtons = usePaginationButtons(setCurrentPage, currentPage, friends);
    const refreshInfo = useRefreshableData(fetchData, 5);

    return (
        <>
            {modal}
            <div>
                <div className="page-container">
                    <h1 className="text-center" style={{ marginTop: '30px' }}>{id ? <>Amigos de {getCurrentUser()?.username}</> : 'Tus amigos'}</h1>
                    <div style={{ display: 'flex', justifyContent: 'flex-end', marginRight: '10px' }}>
                        {refreshInfo}
                    </div>
                    <div>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'center',
                        }}>
                            <DButton style={{ width: '25vw' }} onClick={setAddFriendModal}>Añadir amigo</DButton>
                        </div>
                        <Table aria-label="player" className="mt-4">
                            <thead>
                                <tr>
                                    <th className="text-center">Icono</th>
                                    <th className="text-center">Usuario</th>
                                    {user.is_admin ? <th className="text-center">Correo Electrónico</th> : undefined}
                                    {user.is_admin ? <th className="text-center">Rol</th> : undefined}
                                    <th className="text-center">{id ? 'Acciones' : 'Acción'}</th>
                                    {user.is_admin ? <th className="text-center"></th> : undefined}
                                </tr>
                            </thead>
                            <tbody>
                                {friends.length > 0 ? friendsList : undefined}
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

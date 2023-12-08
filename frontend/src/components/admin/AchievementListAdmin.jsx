import { Table } from "reactstrap";
import { useEffect, useState } from "react";
import DButton from "../ui/DButton";
import getModal from "../../util/getModal";
import { Link } from "react-router-dom";
import axios from '../../services/api';
import imgnotfound from '../../static/images/defaultAchievementImg.png'; 

export default function AchievementListAdmin() {
    const [message, setMessage] = useState();
    const [modalHeader, setModalHeader] = useState();
    const [modalActions, setModalActions] = useState();
    const [achievements, setAchievements] = useState([]);

    async function fetchData() {
        try {
            const response = await axios.get('/achievements');
            setAchievements(response.data);
        } catch (error) {
            setModalHeader('Error');
            setMessage("Error al obtener logros.");
        }
    }

    async function deleteAchievement(id) {
        try {
            await axios.delete(`/achievements/${id}`);
        } catch (e) {
            setModalHeader("Error al borrar el logro");
            if (e.response.status === 401) {
                setMessage("El usuario actual no es administrador, no tiene permisos para borrar este logro");
            } else if (e.response.status === 404) {
                setMessage("El logro no existe");
            } else if (e.response.status === 500) {
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
                deleteAchievement(id);
                clear();
                }}>
                    Confirmar
            </DButton>
        </>);
        setModalHeader("Va a eliminar un logro");
        setMessage("¿Continuar?");
    }

    useEffect(() => {
        fetchData();
    } , []);

    const achievementList =
        achievements.map((a) => {
            return (
                <tr key={a.id}>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> <img src={a.badgeImage || imgnotfound} alt={""} width="50px" /></td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>{a.name}</td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {a.description} </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {a.threshold} </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> {a.metric} </td>
                    <td className="text-center">
                        <Link to={`/achievements/edit/${a.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
                            <DButton style={{ width: '15vw', backgroundColor: '#ffcc24', color: 'black' }}>
                                Editar
                            </DButton>
                        </Link>
                    </td>
                    <td className="text-center">
                        <DButton
                            style={{ width: '15vw', backgroundColor: '#ff3300', color: 'black' }}
                            onClick={() => setConfirmActions(a.id)}>
                            Borrar
                        </DButton>
                    </td>
                </tr>
            );
        });

    const modal = getModal(setMessage, message, modalHeader, modalActions);

    return (
        <div>
            <div className="admin-page-container">
                <h1 className="text-center" style={{ marginTop: '30px' }}>Logros</h1>
                {modal}
                <div>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                    }}>
                        <Link 
                            to={`/achievements/new`}
                            style={{ textDecoration: "none" }}
                        > 
                            <DButton style={{ width: '25vw' }}>Crear logro</DButton>
                        </Link>
                    </div>
                    <Table aria-label="achievements" className="mt-4">
                        <thead>
                            <tr>
                                <th className="text-center">Icono</th>
                                <th className="text-center">Nombre</th>
                                <th className="text-center">Descripción</th>
                                <th className="text-center">Límite</th>
                                <th className="text-center">Métrica</th>
                                <th className="text-center">Acciones</th>
                                <th className="text-center"></th>
                            </tr>
                        </thead>
                        <tbody>{achievementList}</tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

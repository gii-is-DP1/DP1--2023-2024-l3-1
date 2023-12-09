import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Table } from "reactstrap";
import DButton from "../../../components/ui/DButton";
import { useModal } from "../../../composables/useModal";
import { usePaginationButtons } from "../../../composables/usePaginationButtons";
import { useRefreshableData } from "../../../composables/useRefreshableData";
import { achievementTranslation } from "../../../models/maps";
import axios from '../../../services/api';
import imgnotfound from '../../../static/images/default_achievement.png';
import { dividirArray } from "../../../util/dataManipulation";

export default function AchievementListAdminPage() {
    const [message, setMessage] = useState();
    const [currentPage, setCurrentPage] = useState(0);
    const [modalHeader, setModalHeader] = useState();
    const [modalActions, setModalActions] = useState();
    const [achievements, setAchievements] = useState([[]]);

    async function fetchData() {
        try {
            const response = await axios.get('/achievements');
            if (response.status === 204) {
                setMessage("No hay logros que mostrar");
                return;
            }
            // Para probar la paginación, cambiar este número
            setAchievements(dividirArray(response.data, 10));
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
            if (e.response?.status === 401) {
                setMessage("El usuario actual no es administrador, no tiene permisos para borrar este logro");
            } else if (e.response?.status === 404) {
                setMessage("El logro no existe");
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
            <DButton color="red" onClick={() => clear()}>
                Cancelar
            </DButton>
            <DButton color="green" onClick={() => {
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
        achievements[currentPage].map((a) => {
            return (
                <tr key={a.id}>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>
                        <img src={a.badgeImage || imgnotfound} alt={""} width="50px" />
                    </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>
                        {a.name}
                    </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>
                        {a.description}
                    </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>
                        {a.threshold}
                    </td>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}>
                        {achievementTranslation[a.metric]}
                    </td>
                    <td className="text-center">
                        <Link to={`/achievements/edit/${a.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
                            <DButton color="yellow" style={{ width: '15vw' }}>
                                Editar
                            </DButton>
                        </Link>
                    </td>
                    <td className="text-center">
                        <DButton
                            color="red"
                            onClick={() => setConfirmActions(a.id)}>
                            Borrar
                        </DButton>
                    </td>
                </tr>
            );
        });

    const modal = useModal(setMessage, message, modalHeader, modalActions);
    const paginationButtons = usePaginationButtons(setCurrentPage, currentPage, achievements);
    const refreshInfo = useRefreshableData(fetchData, 5);

    return (
        <>
        {modal}
        <div>
            <div className="page-container">
                <h1 className="text-center" style={{ marginTop: '30px' }}>Logros</h1>
                <div style={{ display: 'flex', justifyContent: 'flex-end', marginRight: '10px' }}>
                    {refreshInfo}
                </div>
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
                    <p>La información se actualiza automáticamente cada 5 segundos</p>
                    {paginationButtons}
                </div>
            </div>
        </div>
        </>
    );
}

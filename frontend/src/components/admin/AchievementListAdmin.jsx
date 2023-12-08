import { Table } from "reactstrap";
import { useEffect, useState } from "react";
import deleteFromList from "../../../deprecations/util/deleteFromList";
import getErrorModal from "../../util/getModal";
import { Link } from "react-router-dom";
import axios from '../../services/api';
import imgnotfound from '../../static/images/defaultAchievementImg.png'; 

export default function AchievementListAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [achievements, setAchievements] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/achievements');
                setAchievements(response.data);
            } catch(error) {
                console.error('Error al obtener los logros: ', error);
            }
        };
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
                        <Link to={`/achievements/${a.id}`} style={{ textDecoration: "none", marginLeft: "30px" }}>
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
                                    `/api/v1/achievements/${a.id}`,
                                    a.id,
                                    [achievements, setAchievements],
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
                <h1 className="text-center" style={{ marginTop: '30px' }}>Logros</h1>
                {alerts.map((a) => a.alert)}
                {modal}
                <div>
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
                    <div className="custom-button-row">
                        <Link 
                            to={`/achievements/new`}
                            style={{ textDecoration: "none" }}
                        > 
                            <button className="auth-button"> Crear logro </button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

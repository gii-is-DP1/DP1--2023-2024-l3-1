import { Button, Table } from "reactstrap";
import { useState } from "react";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import { Link } from "react-router-dom";


const imgnotfound = 'https://cdn-icons-png.flaticon.com/512/5778/5778223.png'; 
//const imgnotfound = '../../static/images/defaultAchievementImg.png';
const jwt = tokenService.localAccessToken;

export default function AchievementListAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [alerts, setAlerts] = useState([]);
    const [achievements, setAchievements] = useFetchState(
        [],
        `/api/v1/achievements`,
        jwt
    );
    const achievementList =
        achievements.map((a) => {
            return (
                <tr key={a.id}>
                    <td className="text-center" style={{ verticalAlign: 'middle' }}> <img src={a.badgeImage ? a.badgeImage : imgnotfound} alt={""} width="50px" /></td>
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

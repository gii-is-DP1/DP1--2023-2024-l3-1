import {
    Button,
    Table
} from "reactstrap";
import { useState } from "react";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import { Link } from "react-router-dom";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
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
                    <td className="text-center"> <img src={a.badgeImage ? a.badgeImage : imgnotfound} alt={a.name} width="50px" /></td>
                    <td className="text-center">{a.name}</td>
                    <td className="text-center"> {a.description} </td>
                    <td className="text-center"> {a.threshold} </td>
                    <td className="text-center"> {a.metric} </td>
                    <td className="text-center">
                        <Button outline color="warning" >
                            <Link
                                to={`/achievements/` + a.id} className="btn sm"
                                style={{ textDecoration: "none" }}>Editar</Link>
                        </Button>
                    </td>
                    <td className="text-center">
                        <Button outline color="danger"
                            onClick={() =>
                                deleteFromList(
                                    `/api/v1/achievements/${a.id}`,
                                    a.id,
                                    [achievements, setAchievements],
                                    [alerts, setAlerts],
                                    setMessage,
                                    setVisible
                                )}>
                            Borrar
                        </Button>
                    </td>
                </tr>
            );
        });
    const modal = getErrorModal(setVisible, visible, message);
    return (
        <div>
            <div> <h1> </h1> </div>
            <div className="admin-page-container">
                <h1 className="text-center">Logros</h1>
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
                    <Button outline color="success" >
                        <Link
                            to={`/achievements/new`} className="btn sm"
                            style={{ textDecoration: "none" }}>Crear logro</Link>
                    </Button>
                </div>
            </div>
        </div>
    );
}

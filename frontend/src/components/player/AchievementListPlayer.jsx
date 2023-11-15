import {
    Table
} from "reactstrap";
import { useState } from "react";
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import getErrorModal from "../../util/getErrorModal";
const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.localAccessToken;
export default function AchievementListPlayer() {
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
                    <td className="text-center"><img src={a.badgeImage ? a.badgeImage : imgnotfound} alt={a.name} width="50px" /></td>
                    <td className="text-center">{a.name}</td>
                    <td className="text-center"> {a.description} </td>
                    
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
                                <th className="text-center">Descripción</th>
                                <th className="text-center">Nombre</th>
                            </tr>
                        </thead>
                        <tbody>{achievementList}</tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

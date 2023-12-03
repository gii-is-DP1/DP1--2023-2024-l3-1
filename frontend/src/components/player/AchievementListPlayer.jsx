import { Table } from "reactstrap";
import { useEffect, useState } from "react";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';
import imgnotfound from '../../static/images/defaultAchievementImg.png';

export default function AchievementListPlayer() {

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
                    <td style={{ verticalAlign: 'middle' }}> <img src={a.badgeImage || imgnotfound} alt={" "} width="50px" /></td>
                    <td style={{ verticalAlign: 'middle' }}> {a.name} :</td>
                    <td style={{ verticalAlign: 'middle' }}> {a.description} </td>
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
                        <tbody> {achievementList} </tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

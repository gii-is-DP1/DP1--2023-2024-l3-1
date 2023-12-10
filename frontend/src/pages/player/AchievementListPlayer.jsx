import { useEffect, useState } from "react";
import { Table } from "reactstrap";
import { useModal } from "../../composables/useModal";
import { usePaginationButtons } from "../../composables/usePaginationButtons";
import { useRefreshableData } from "../../composables/useRefreshableData";
import axios from '../../services/api';
import imgnotfound from '../../static/images/default_achievement.png';
import { dividirArray } from "../../util/dataManipulation";

export default function AchievementListPlayer() {
    const [message, setMessage] = useState(null);
    const [achievements, setAchievements] = useState([[]]);
    const [currentPage, setCurrentPage] = useState(0);

    async function fetchData() {
        try {
            const response = await axios.get('/achievements');
            if (response.status === 204) {
                setMessage("No hay logros que mostrar");
                return;
            }

            setAchievements(dividirArray(response.data, 10));
        } catch (e) {
            setMessage(String(e));
        }
    }

    useEffect(() => {
        fetchData();
    }, []);

    const achievementList =
        achievements[currentPage].map((a) => {
            return (
                <tr key={a.id} style={{ verticalAlign: 'middle' }}>
                    <td className="text-center">
                        <img src={a.badgeImage || imgnotfound} alt={" "} width="50px" />
                    </td>
                    <td className="text-center">
                        {a.name}
                    </td>
                    <td className="text-center">
                        {a.description}
                    </td>
                </tr>
            );
        });

    const modal = useModal(setMessage, message, 'Error');
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
                        <Table aria-label="achievements" className="mt-4">
                            <thead>
                                <tr>
                                    <th className="text-center"></th>
                                    <th className="text-center">Nombre</th>
                                    <th className="text-center">Descripción</th>
                                </tr>
                            </thead>
                            <tbody>
                                {achievementList}
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

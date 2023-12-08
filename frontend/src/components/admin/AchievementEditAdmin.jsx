import { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Form, Input } from "reactstrap";
import getErrorModal from "../../util/getModal";
import DInput from "../ui/DInput"
import axios from '../../services/api';
import { formStyle } from "../ui/styles/forms";

export default function AchievementEditAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [loading, setLoading] = useState(false);
    const [achievement, setAchievement] = useState({});
    const navigate = useNavigate();

    const { id } = useParams();

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setAchievement({ ...achievement, [name]: value });
    }

    async function request() {
        try {
            setMessage(null);
            if (id) {
                const response = await axios.get(`/achievements/${id}`);
                if (response.status === 401) {
                    setMessage("Logro no existe");
                    return;
                } else if (response.status >= 500) {
                    setMessage("Error del servidor");
                    return;
                }
                setAchievement(response.data);
            } else {
                setAchievement({});
            }
        } catch (e) {
            setMessage(String(e));
        }
    }

    useEffect(() => {
        const run = async () => {
            await request();
        }
        run();
    }, []);

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            setLoading(true);
            setMessage(null);
            if (id) {
                const response = await axios.patch(`/achievements/${id}`, achievement);
                setAchievement(response);
            } else {
                const response = await axios.post(`/achievements/new`, achievement);
                setAchievement(response);
            }

            navigate("/achievements")
        } catch (e) {
            setMessage(String(e));
        }
    }

    const modal = getErrorModal(setVisible, visible, message);

    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {achievement.id ? "Editar logro" : "Añadir logro"}
            </h2>
            {modal}
            <div className="auth-form-container">
                <Form onSubmit={handleSubmit} style={formStyle}>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            required
                            name="name"
                            id="name"
                            placeholder="Nombre"
                            value={achievement.name || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            required
                            name="description"
                            id="description"
                            placeholder="Descripción"
                            value={achievement.description || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-form-input">
                        <DInput
                            type="text"
                            name="badgeImage"
                            id="badgeImage"
                            placeholder="URL imagen logro"
                            value={achievement.badgeImage || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-form-input">
                        <Input
                            type="select"
                            required
                            name="metric"
                            id="metric"
                            value={achievement.metric || ""}
                            onChange={handleChange}
                            className="custom-input-metric"
                        >
                            <option value="">Métrica</option>
                            <option value="GAMES_PLAYED">Partidas jugadas</option>
                            <option value="VICTORIES">Victorias</option>
                            <option value="TOTAL_PLAY_TIME">Tiempo total de juego</option>
                            <option value="REACTION_TIME">Tiempo de reacción</option>
                        </Input>
                    </div>
                    <div className="custom-form-input">
                        <DInput
                            type="number"
                            required
                            name="threshold"
                            id="threshold"
                            placeholder="Valor límite de la métrica"
                            value={achievement.threshold || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-button-row">
                        <button type="submit" className="auth-button"> {loading ? 'Guardando...' : 'Guardar cambios'} </button>
                        <Link
                            to={`/achievements`}
                            className="auth-button-red"
                            style={{ textDecoration: "none" }}
                        >
                            Descartar cambios
                        </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
} 

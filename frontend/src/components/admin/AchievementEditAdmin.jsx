import { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Form } from "reactstrap";
import getModal from "../../util/getModal";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput"
import axios from '../../services/api';
import { formStyle } from "../ui/styles/forms";

export default function AchievementEditAdmin() {
    const [message, setMessage] = useState();
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
            setLoading(true);
            setMessage();
            if (id) {
                const response = await axios.get(`/achievements/${id}`);
                setAchievement(response.data);
            } else {
                setAchievement({});
            }
        } catch (e) {
            if (e.response.status === 401) {
                setMessage("El usuario actual no ha iniciado sesión, no tiene permisos para obtener logros")
            } else if (e.response.status === 404) {
                setMessage("El logro no existe");
            } else if (e.response.status === 500) {
                setMessage("Error interno del servidor");
            } else {
                setMessage(String(e));
            }
        } finally {
            setLoading(false);
        }
    }

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            setMessage();
            if (id) {
                const response = await axios.patch(`/achievements/${id}`, achievement);
                setAchievement(response);
            } else {
                const response = await axios.post(`/achievements/new`, achievement);
                setAchievement(response);
            }

            navigate("/achievements")
        } catch (e) {
            if (e.response.status === 401 && id) {
                setMessage("El usuario actual no es administrador, no tiene permisos para editar logros")
            } else if (e.response.status === 401 && !id) {
                setMessage("El usuario actual no es administrador, no tiene permisos para crear logros")
            } else if (e.response.status === 404 && id) {
                setMessage("El logro a editar no existe");
            } else if (e.response.status === 500) {
                setMessage("Error interno del servidor");
            } else {
                setMessage(String(e));
            }
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        const run = async () => {
            await request();
        }
        run();
    }, []);

    const modal = getModal(setMessage, message, 'Error');

    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {achievement.id ? "Editar logro" : "Añadir logro"}
            </h2>
            {modal}
            <div>
                <Form onSubmit={handleSubmit} style={formStyle}>
                    <DInput
                        type="text"
                        required
                        name="name"
                        id="name"
                        placeholder="Nombre"
                        value={achievement.name || ""}
                        onChange={handleChange}
                    />
                    <DInput
                        type="text"
                        required
                        name="description"
                        id="description"
                        placeholder="Descripción"
                        value={achievement.description || ""}
                        onChange={handleChange}
                    />
                    <DInput
                        type="text"
                        name="badgeImage"
                        id="badgeImage"
                        placeholder="URL imagen logro"
                        value={achievement.badgeImage || ""}
                        onChange={handleChange}
                    />
                    <DInput
                        type="select"
                        required
                        name="metric"
                        id="metric"
                        value={achievement.metric || ""}
                        onChange={handleChange}
                    >
                        <option value="">Métrica</option>
                        <option value="GAMES_PLAYED">Partidas jugadas</option>
                        <option value="VICTORIES">Victorias</option>
                        <option value="TOTAL_PLAY_TIME">Tiempo total de juego</option>
                        <option value="REACTION_TIME">Tiempo de reacción</option>
                    </DInput>
                    <DInput
                        type="number"
                        required
                        name="threshold"
                        id="threshold"
                        placeholder="Valor límite de la métrica"
                        value={achievement.threshold || ""}
                        onChange={handleChange}
                    />
                    <div>
                        <Link
                            to={`/achievements`}
                            style={{ textDecoration: "none" }}
                        >
                            <DButton style={{ backgroundColor: 'red' }}>
                                Cancelar
                            </DButton>
                        </Link>
                        <DButton type="submit">
                            {loading ? 'Guardando...' : 'Guardar cambios'}
                        </DButton>
                    </div>
                </Form>
            </div>
        </div>
    );
} 

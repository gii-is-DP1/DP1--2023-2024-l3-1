import { useState } from "react";
import tokenService from "../../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchState from "../../util/useFetchState";
import DInput from "../ui/DInput"

const jwt = tokenService.localAccessToken;

export default function AchievementEditAdmin() {
    const id = getIdFromUrl(2);
    const emptyAchievement = {
        id: id === "new" ? null : id,
        name: "",
        description: "",
        badgeImage: "",
        threshold: 1,
        metric: "GAMES_PLAYED",
        actualDescription: ""
    };
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [achievement, setAchievement] = useFetchState(
        emptyAchievement,
        `/api/v1/achievements/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    );
    const modal = getErrorModal(setVisible, visible, message);
    function handleSubmit(event) {
        event.preventDefault();
        fetch(
            "/api/v1/achievements" + (achievement.id ? "/" + achievement.id : ""),
            {
                method: achievement.id ? "PUT" : "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(achievement),
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if (data === "")
                    window.location.href = "/achievements";
                else {
                    let json = JSON.parse(data);
                    if (json.message) {
                        setMessage(JSON.parse(data).message);
                        setVisible(true);
                    } else
                        window.location.href = "/achievements";
                }
            })
            .catch((message) => alert(message));
    }
    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setAchievement({ ...achievement, [name]: value });
    }
    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {achievement.id ? "Editar logro" : "Añadir logro"}
            </h2>
            <div className="auth-form-container">
                {modal}
                <Form onSubmit={handleSubmit}>
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
                            id="descripction"
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
                        <Label for="metric" style={{ fontSize: '20px' }}>
                            Métrica:
                        </Label>
                        <Input
                            type="select"
                            required
                            name="metric"
                            id="metric"
                            value={achievement.metric || ""}
                            onChange={handleChange}
                            className="custom-input-metric"
                        >
                            <option value="GAMES_PLAYED">Partidas jugadas</option>
                            <option value="VICTORIES">Victorias</option>
                            <option value="TOTAL_PLAY_TIME">Tiempo total de juego</option>
                            <option value="REACTION_TIME">Tiempo de reacción</option>
                        </Input>
                    </div>
                    <div className="custom-form-input">
                        <Label for="theshold" style={{ fontSize: '20px' }}>
                            Valor límite:
                        </Label>
                        <DInput
                            type="number"
                            required
                            name="threshold"
                            id="threshold"
                            placeholder="test"
                            value={achievement.threshold || ""}
                            onChange={handleChange}
                            style={{width: '300px'}}
                            //style={{width: '100px'}}
                        />
                    </div>
                    <div className="custom-button-row">
                        <button className="auth-button">Guardar cambios</button>
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

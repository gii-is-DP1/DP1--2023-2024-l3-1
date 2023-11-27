import { useState, useEffect } from "react";
import { Link,  useNavigate } from "react-router-dom";
import { Label } from "reactstrap";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import DInput from "../ui/DInput";
import axios from '../../services/api';
import { formStyle } from "../ui/styles/forms";

export default function PlayerEditAdmin() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [player, setPlayer] = useState({});
    const navigate = useNavigate();

    const id = getIdFromUrl(2);
 
    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setPlayer({ ...player, [name]: value });
    }

    async function request() {
        try {
            setMessage(null);
            if(id){
                const response = await axios.get(`/player/${id}`);
                if (response.status === 401) {
                    setMessage("Usuario no existe");
                    return;
                } else if (response.status >= 500) {
                    setMessage("Error del servidor");
                    return;
                }
                setPlayer(response.data);
            }else{
                setPlayer({});
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
            setMessage(null);
            if (id!=="new" && id!==null) {
                const response = await axios.patch(`/player/${id}`, player);
                setPlayer(response);
            } else {
                const response = await axios.post(`/player/new`, player);
                setPlayer(response);
            }
      
            navigate("/player")
        } catch (e) {
            setMessage(String(e));
        }
    }

    const modal = getErrorModal(setVisible, visible, message);
 
    return (
        <div className="auth-page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                {player.id ? "Editar Jugador" : "Añadir Jugador"}
            </h2>
            {modal}
            <div className="auth-form-container">
            <form onSubmit={handleSubmit} style={formStyle}>
                    <div className="custom-form-input">
                        <Label for="username">
                            Username
                        </Label>
                        <DInput
                            type="text"
                            required
                            name="username"
                            id="username"
                            value={player.username || ""}
                            onChange={handleChange}
                        />
                    </div>
               
                    <div className="custom-form-input">
                        <Label for="email">
                            Email
                        </Label>
                        <DInput
                            type="text"
                            required
                            name="email"
                            id="email"
                            value={player.email || ""}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="custom-form-input">
                        <Label for="password">
                            Password
                        </Label>
                        <DInput
                            type="text"
                            required
                            name="password"
                            id="password"
                            value={player.password || ""}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-button-row">                        
                        <button type="submit"
                         className="auth-button">Guardar</button>
                        <Link
                            to={`/player`}
                            className="auth-button-red"
                            style={{ textDecoration: "none" }}
                        >
                            Cancelar
                        </Link>
                    </div>
                </form>
            </div>
        </div>
    );
} 

import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Alert } from "reactstrap";
import { adjectives, animals, colors, countries, languages, names, starWars, uniqueNamesGenerator } from 'unique-names-generator';
import DButton from "../../components/ui/DButton";
import DInput from "../../components/ui/DInput";
import { formStyle } from "../../components/ui/styles/forms";
import { useLabeledForm } from "../../composables/useLabeledForm";
import axios from '../../services/api';

export default function CreationGamePage() {
    const generateName = () => {
        return uniqueNamesGenerator({
            dictionaries: [adjectives, animals, colors, countries, names, languages, starWars],
            separator: '-',
            length: 3
        });
    }
    const [message, setMessage] = useState();
    const [suggestedName, setSuggestedName] = useState(generateName());
    const [game, setGame] = useState({ 
        max_players: 8 
    });
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    function handleChange(event) {
        event.preventDefault();
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setGame({ ...game, [name]: value });
    }

    function handleIncrement(e) {
        e.preventDefault();
        if (game.max_players < 8) {
            setGame(prevGame => ({ ...prevGame, max_players: prevGame.max_players + 1 }));
        }
    }

    function handleDecrement(e) {
        e.preventDefault();
        if (game.max_players > 2) {
            setGame(prevGame => ({ ...prevGame, max_players: prevGame.max_players - 1 }));
        }
    }

    async function handleSubmit(event) {
        try {
            event.preventDefault();
            setLoading(true);
            setMessage();

            const data = {
                ...game,
                ...(!game?.name?.trim() && { name: suggestedName })
            };
    
            const response = await axios.post("/games", data);
            navigate(`/play/${response.data.id}`)
        } catch (e) {
            if (e.response?.status >= 400) {
                setMessage("Error del cliente");
                return;
            } else if (e.response?.status >= 500) {
                setMessage("Error del servidor");
                return;
            }
            setMessage(String(e));
        } finally {
            setLoading(false);
        }
    }

    const form = useLabeledForm(
        {
            'Nombre de la partida': 
                (
                    <DInput
                        placeholder={suggestedName}
                        type="text"
                        onChange={handleChange}
                    />
                ),
            'Numero máximo de jugadores': 
                (<>
                    <DButton color="red" onClick={handleDecrement} disabled={game.max_players === 2}>-</DButton>
                    {game.max_players} / 8
                    <DButton color="green" onClick={handleIncrement} disabled={game.max_players === 8}>+</DButton>
                </>)
        }
    );

    return (
        <div className="page-container">
            <h2 className="text-center" style={{ marginTop: '30px' }}>
                Crear nueva partida
            </h2>
            {message ? (
                <Alert color="primary">{message}</Alert>
                ) : undefined}
            <div>
                <form onSubmit={handleSubmit} style={formStyle}>
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                        <p>¿No sabes que nombre escoger? ¡Te sugerimos <b>{suggestedName}</b>!</p>
                        <p>(Deja vacío el nombre para aplicarlo)</p>
                    </div>
                    {form}
                    <div style={{...formStyle, flexDirection: 'row' }}>
                        <Link
                            to={`/play/choose`}
                            style={{ textDecoration: "none" }}
                        >
                            <DButton color="red" style={{ width: '25vw' }} disabled={loading}>
                                Cancelar
                            </DButton>
                        </Link>
                        <DButton style={{ width: '25vw' }} disabled={loading}>
                            {loading ? 'Guardando...' : 'Continuar'}
                        </DButton>
                    </div>
                    <DButton style={{ width: '25vw' }} color="yellow" disabled={loading} 
                        onClick={(e) => {
                            e.preventDefault();
                            setSuggestedName(generateName());
                        }
                    }>
                        Nuevo nombre aleatorio
                    </DButton>
                </form>
            </div>
        </div>
    );
}

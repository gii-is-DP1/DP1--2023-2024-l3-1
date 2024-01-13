import { useLocation, useNavigate } from "react-router-dom";
import { useLabeledForm } from "../../composables/useLabeledForm";
import axios from "../../services/api";
import NumberOfPlayers from "../forms/NumberOfPlayers";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import UserAvatar from "./UserAvatar";

export default function GameLobby(props) {
    const route = useLocation();
    const navigate = useNavigate();
    const getSharingUrl = () => `${window.location.origin}${route.pathname}`;

    function handleChange(event) {
        event.preventDefault();
        const target = event.target;
        const value = target.value;
        const name = target.name;
        props.setGame({ ...props.game, [name]: value });
    }

    async function removeFromGame(player) {
        try {
            await axios.delete(`/games/${player}`);

            if (player.username === props.game.creator.username) {
                navigate('/')
            }
        } catch {}
    }

    async function startGame() {
        try {
            await axios.post('/games/start');
        } catch (e) {
            props.setHeader("Error al iniciar la partida");

            if (e.response?.status === 304) {
                props.setMessage("Ya estás unido a esta partida");
            } else if (e.response?.status === 404) {
                props.setMessage("No se encuentra la partida.");
            } else if (e.response?.status === 401) {
                props.setMessage("El jugador actual no es el creador de la partida o no está autenticado.");
            } else if (e.response?.status === 423) {
                props.setMessage("La partida no está en el estado correcto para iniciar (está en curso o finalizada).");
            } else if (e.response?.status === 428) {
                props.setMessage("No hay suficientes jugadores para iniciar la partida.");
            } else if (e.response?.status === 500) {
                props.setMessage("Error del servidor");
            }
        }
    }

    const adminControls = useLabeledForm(
        {
            'Comparte tu url:':
            (
                <DInput type="text" value={getSharingUrl()} disabled style={{ width: '100%' }} />
            ),
            'Editar nombre: ': (
                <DInput type="text" name="name" placeholder={props.game.name} onChange={handleChange} style={{ width: '100%' }} />
            ),
            'Número máximo de jugadores': (
                <NumberOfPlayers game={props.game} setGame={props.setGame} />
            )
        }
    );

    return (
        <div style={{ margin: '50px' }}>
            <h1 className="text-center" style={{ marginTop: '30px' }}>
                Lobby de la partida <i>{props.game.name}</i>
            </h1>
            {props.is_creator ? adminControls : undefined}
            <h3>Jugadores Conectados:</h3>
            <table style={{ width: '100%' }}>
            <tbody>
                {props.game.game_players.map((player, index) => (
                    <tr key={index}>
                        <td style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                            <div style={{ display: 'flex', alignItems: 'center' }}>
                                <div>
                                    <UserAvatar user={player} size="small" color={player.username === props.game.creator.username ? 'yellow' : undefined}/>
                                </div>
                                <p style={{ marginBottom: '0', marginTop: '6px' }}>{player.username}</p>
                            </div>
                            {props.is_creator ?
                                <DButton
                                    color="red"
                                    onClick={() => removeFromGame(player.username)}>
                                        Eliminar
                                </DButton>
                            : undefined}
                        </td>
                    </tr>
                    )
                )}
            </tbody>
        </table>
            <ul>
                
            </ul>
            {props.is_creator ? (
                <DButton style={{ width: '100%' }} disabled={!props.game.startable} onClick={startGame}>
                    Iniciar Partida
                </DButton>
            ) : undefined}
        </div>
    );
}

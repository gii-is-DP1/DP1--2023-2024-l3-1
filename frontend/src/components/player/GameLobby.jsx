import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { useLabeledForm } from "../../composables/useLabeledForm";
import axios from "../../services/api";
import NumberOfPlayers from "../forms/NumberOfPlayers";
import DButton from "../ui/DButton";
import DInput from "../ui/DInput";
import UserAvatar from "./UserAvatar";

export default function GameLobby(props) {
    const user = useSelector(state => state.appStore.user);
    const route = useLocation();
    const isCreator = () => user.username === props.game.creator.username;
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
            await axios.delete(`/games/${player}`)
        } catch {}
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
            {isCreator() ? adminControls : undefined}
            <h3>Jugadores Conectados:</h3>
            <table style={{ width: '100%' }}>
            <tbody>
                {props.game.players.map((player, index) => (
                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                        <div>
                            <UserAvatar user={player} size="small" />
                        </div>
                        <p style={{ marginBottom: '0', marginTop: '6px' }}>{player.username}</p>
                        {isCreator() ?
                            <DButton
                                color="red"
                                onClick={removeFromGame(player)}>
                                    Eliminar
                            </DButton>
                        : undefined}
                    </div>
                    )
                )}
            </tbody>
        </table>
            <ul>
                
            </ul>
            {isCreator() ? (
                <DButton style={{ width: '100%' }} disabled={props.game.players?.length <= 1}>
                    Iniciar Partida
                </DButton>
            ) : undefined}
        </div>
    );
}

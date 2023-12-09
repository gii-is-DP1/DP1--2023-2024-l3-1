import { Link } from "react-router-dom";
import DButton from "../../components/ui/DButton";
import "../../static/css/play/playPage.css";

const lobbyButtonStyle = {
    backgroundColor: 'white',
    color: 'black',
    padding: '8px 40px',
    margin: '40px',
    border: '1px solid #ccc',
    borderRadius: '15px',
}

export default function PlayPage() {
    return (
        <div className="page-container">
            <div className="button-container">
                <Link to={"join"}>
                    <DButton style={lobbyButtonStyle}>Unirse a partida</DButton>
                </Link>
                <DButton style={lobbyButtonStyle}>Crear partida</DButton>
            </div>
        </div>
    );
}

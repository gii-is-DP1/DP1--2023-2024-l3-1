import { Link } from "react-router-dom";
import DButton from "../../components/ui/DButton";
import "../../static/css/play/playPage.css";

const lobbyButtonStyle = {
    padding: '8px 40px',
    margin: '40px',
    border: '1px solid #ccc',
    width: '25vw',
    borderRadius: '15px',
}

export default function PlayPage() {
    return (
        <div className="page-container">
            <div className="button-container">
                <Link to={"/play/join"}>
                    <DButton color="white" style={lobbyButtonStyle}>Unirse a partida</DButton>
                </Link>
                <Link to={"/play/new"}>
                    <DButton color="white" style={lobbyButtonStyle}>Crear partida</DButton>
                </Link>
            </div>
        </div>
    );
}

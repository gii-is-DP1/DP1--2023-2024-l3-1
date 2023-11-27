import { Link } from "react-router-dom";
import "../../static/css/play/playPage.css";

export default function PlayPage() {
    return (
        <div className="page-container">
            <div className="button-container">
            <Link to={"join"}>
            <button className="button">Unirse a partida</button>   
            </Link>
            <button className="button">Crear partida</button>
            
            </div>
        </div>

    );
}
import { Link } from "react-router-dom";
import "../../static/css/play/playPage.css";
import DButton from "../ui/DButton";

export const inputStyles = {
    /*background-color: 'white';
    color: black !important;
    padding: 8px 40px !important;
    margin: 40px !important;
    border: 1px solid #ccc !important;
    border-radius: 15px !important;
    */
}

export default function PlayPage() {
    
    return (
        <div className="page-container">
            <div className="button-container">
            <Link to={"join"}>
            <DButton className="button">Unirse a partida</DButton>   
            </Link>
            <DButton className="button">Crear partida</DButton>
            
            </div>
        </div>

    );
}
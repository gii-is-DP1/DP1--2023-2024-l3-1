import { Alert} from "reactstrap";
import "../../../src/static/css/profile/profilePage.css";
import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';
import DButton from "../ui/DButton";



export default function PlayerProfile(){
    const [currentUser, setCurrentUser] = useState([]);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    async function request() {
        try {
          setMessage(null);
    
          const response = await axios.get("/player/me");
          if (response.status === 401) {
            setMessage("Usuario actual no autenticado");
            return;
          } else if (response.status >= 500) {
            setMessage("Error del servidor");
            return;
          }
    
          setCurrentUser(response.data);
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

 
  const modal = getErrorModal(setVisible, visible, message);

    return(
    <div className="profile-page-container">
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}
        {modal}
        <h1>Mi perfil</h1>
        <div className="profile-field">
            <h6>Nombre de usuario: </h6>
        <div className="rounded-box">{currentUser.username}</div>
        </div>
        
        <div className="profile-field">
            <h6>Email: </h6>
        <div className="rounded-box">{currentUser.email}</div>
        </div>

        <div className="profile-field">
            <h6>Logo:</h6>
        </div>

        <div>
        <Link  to={"/edit"}>
        <DButton text={ 'Editar perfil' } style={{ width: '25vw' } } />
        </Link>
        
        </div>
    </div>
    );
}
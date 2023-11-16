import { Alert} from "reactstrap";
import "../../../src/static/css/profile/profilePage.css";
import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';
//import tokenService from "../../services/token.service";



export default function PlayerProfile(){
    const [currentUser, setCurrentUser] = useState([]);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    //const jwt = tokenService.localAccessToken;

    async function request() {
        try {
          setMessage(null);
    
          const response = await axios.get("/player/me");
          console.log("Response: ")
          console.log(response)
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
        } finally {
          //setLoading(false);
        }     
      }

      
  useEffect(() => {
    const run = async () => {
      await request();
    }
    run();
  }, []);

  //const [alerts, setAlerts] = useState([]);
  const modal = getErrorModal(setVisible, visible, message);

/*
    useEffect(() => {
       
        fetch("/api/v1/player/me", {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            "Content-Type": "application/json",
          },
        })
          .then((response) => response.json())
          .then((data) => {
            setCurrentUser(data);
          })
          .catch((error) => {
            console.error("Error al obtener el usuario actual:", error);
            setMessage("Error al obtener el usuario actual");
          });
      }, []);

*/




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
        <button className="edit-profile-button"
        tag={Link}
        
        >
            Editar perfil
        </button>
        </div>
    </div>
    );




}
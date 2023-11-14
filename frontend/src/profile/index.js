import { Alert } from "reactstrap";
import "../../src/static/css/profile/profilePage.css";
import { useEffect, useState } from "react"
import tokenService from "../services/token.service";




export default function Profile(){
    const [currentUser, setCurrentUser] = useState([]);
    const [message, setMessage] = useState(null);
    const jwt = tokenService.localAccessToken;

    useEffect(() => {
       
        fetch("/api/v1/dobbleusers/current", {
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

    return(
    <div className="profile-page-container">
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}
        
        <h1>Mi perfil</h1>
        <div className="profile-field">
            <h6>Nombre de usuario: </h6>
        <div className="rounded-box">{currentUser.username}</div>
        </div>
        
        <div className="profile-field">
            <h6>Email:</h6>
        <div className="rounded-box">{currentUser.email}</div>
        </div>

        <div className="profile-field">
            <h6>Logo:</h6>
        </div>

    </div>
    );




}

import { Alert } from "reactstrap";
import "../../src/static/css/profile/profilePage.css";
import { useEffect, useState } from "react"
import tokenService from "../services/token.service";




export default function Profile(){
    const [currentUser, setCurrentUser] = useState([]);
    const [message, setMessage] = useState(null);
    const jwt = tokenService.getLocalAccessToken();

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
    <div className="auth-page-container">
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}
        <h1>Mi perfil</h1>
        <h4>Nombre de usuario:  {currentUser.username}</h4>
        <h4>Email:  {currentUser.email}</h4>
    </div>
    );




}
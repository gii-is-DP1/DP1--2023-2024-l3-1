import "../../src/static/css/profile/profilePage.css";
import { useState } from "react"





export default function Profile(){
    const [currentUser,setCurrentUser] = useState(null);
    return(
    <div className="auth-page-container">
        <h1>Perfil de usuario</h1>
    </div>
    );




}
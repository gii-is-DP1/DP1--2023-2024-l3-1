import { useEffect, useState } from "react"
import { Form, Input, Label } from "reactstrap";
import "../../../src/static/css/admin/adminPage.css";
import { Link } from "react-router-dom";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';


export default function ProfileEdit() {
    const emptyItem = {
        id: null,
        is_admin: 0,
        email:"",
        username:"",
        password: "",
        
        
        
      };
    
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [dobbleUser, setDobbleUser] = useState(emptyItem);
    


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setDobbleUser({ ...dobbleUser, [name]: value });
    }

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
    
          setDobbleUser(response.data);
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
    
    
    async function handleSubmit(event) {
        try {
            setMessage(null);
      
            const response = await axios.patch("/player/me",dobbleUser);
            
            if (response.status === 404) {
              setMessage("Error al actualizar el usuario");
              return;
            } else if (response.status >= 500) {
              setMessage("Error del servidor");
              return;
            }
      
            window.location.href = "/profile";
          } catch (e) {
            setMessage(String(e));
          } finally {
            //setLoading(false);
          }  

        

    }

    const modal = getErrorModal(setVisible, visible, message);
    return(
        <div className="auth-page-container">
            <h1>Editar perfil</h1>
            {modal}
            <div className="auth-form-container">
                <Form onSubmit={handleSubmit}>
                    <div className="custom-form-input">
                        <Label for="username" className="custom-form-input-label">
                            Username
                        </Label>
                        <Input
                            type="text"
                            required
                            name="username"
                            id="username"
                            value={dobbleUser.username || ""}
                            className="custom-input"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="custom-form-input">
                        <Label for="username" className="custom-form-input-label">
                            Email
                        </Label>
                        <Input
                            type="text"
                            required
                            name="email"
                            id="email"
                            value={dobbleUser.email || ""}
                            className="custom-input"
                            onChange={handleChange}
                        />
                    </div>
                    <div className="custom-button-row">
                        <button type="submit"
                         className="auth-button">Guardar</button>
                        <Link
                            to={`/profile`}
                            className="auth-button"
                            style={{ textDecoration: "none" }}
                        >
                            Cancelar
                        </Link>
                    </div>
                </Form>
            </div>
        </div>
    );



}

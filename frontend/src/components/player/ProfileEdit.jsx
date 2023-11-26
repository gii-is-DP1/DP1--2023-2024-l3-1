import { useEffect, useState } from "react"
import { Label } from "reactstrap";
import "../../../src/static/css/admin/adminPage.css";
import { Link, useNavigate } from "react-router-dom";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';
import DInput from "../ui/DInput";
import { formStyle } from "../ui/styles/forms";
import DButton from "../ui/DButton";

export default function ProfileEdit() {

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [dobbleUser, setDobbleUser] = useState({});
  const navigate = useNavigate();


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
      event.preventDefault();
      setMessage(null);

      const response = await axios.patch("/player/me", dobbleUser);

      if (response.status === 404) {
        setMessage("Error al actualizar el usuario");
        return;
      } else if (response.status >= 500) {
        setMessage("Error del servidor");
        return;
      }

      navigate("/profile")
    } catch (e) {
      setMessage(String(e));
    }



  }

  const modal = getErrorModal(setVisible, visible, message);
  return (
    <div style={formStyle}>
      <h1>Editar perfil</h1>
      {modal}
      <div className="auth-form-container">
        <form onSubmit={handleSubmit} style={formStyle}>
          <div className="custom-form-input">
            <Label for="username">
              Username
            </Label>
            <DInput
              type="text"
              required
              name="username"
              id="username"
              value={dobbleUser.username || ""}
              onChange={handleChange}
            />
          </div>

          <div className="custom-form-input">
            <Label for="email">
              Email
            </Label>
            <DInput
              type="text"
              required
              name="email"
              id="email"
              value={dobbleUser.email || ""}
              onChange={handleChange}
            />
          </div>
          <div className="custom-button-row">
            <DButton type="submit" text={'Guardar'} style={{ width: '12vw', backgroundColor: '#00cc00' }} />
            
            <Link to={"/profile"}>
              <DButton text={'Cancelar'} style={{ width: '12vw', backgroundColor: '#ff3300' }} />
            </Link>
          </div>
        </form>
      </div>
    </div>
  );



}

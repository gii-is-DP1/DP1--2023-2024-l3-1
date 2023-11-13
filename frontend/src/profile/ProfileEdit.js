import { useState } from "react";
import useFetchState from "../util/useFetchState";
import getIdFromUrl from "../util/getIdFromUrl";
import tokenService from "../services/token.service";
import { Form, Input, Label } from "reactstrap";
import "../static/css/admin/adminPage.css";
import { Link } from "react-router-dom";
import getErrorModal from "../util/getErrorModal";

const jwt = tokenService.getLocalAccessToken();

export default function ProfileEdit() {
    const emptyItem = {
        id: null,
        username: "",
        password: "",
        icon: "",
        email:"",
        friends: [],
      };
    const id = getIdFromUrl(2);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [dobbleUser, setDobbleUser] = useFetchState(
        emptyItem,
        `/api/v1/dobbleusers/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    )
    //const auths = useFetchData(`/api/v1/dobbleusers/authorities`, jwt);


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setDobbleUser({ ...dobbleUser, [name]: value });
    }
    console.log("DOBBLE USER-----------------------------------------------------------------------")
    console.log(dobbleUser)
    function handleSubmit(event) {
       
        fetch("/api/v1/dobbleusers/" +dobbleUser.id, {
            method:  "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dobbleUser),

        })
        .then((response) => response.json())
        .then((json) => {
            if (json.message) {
              setMessage(json.message);
              setVisible(true);
              window.location.href = "/profile"
            } else window.location.href = "/profile";
          })
          .catch((message) => alert(message));
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

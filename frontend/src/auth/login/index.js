import React, { useState } from "react";
import { Alert } from "reactstrap";
import FormGenerator from "../../components/formGenerator/formGenerator";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { loginFormInputs } from "./form/loginFormInputs";

export default function Login() {
  const [message, setMessage] = useState(null)
  const loginFormRef = React.createRef();      
  

  async function handleSubmit({ values }) {

    const reqBody = values;
    setMessage(null);
    await fetch("/api/v1/auth/login", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      .then(function (response) {
        if (response.status === 200) return response.json();
        else return Promise.reject("Intento de inicio de sesión inválido");
      })
      .then(function (data) {
        tokenService.setUser(data);
        tokenService.updateLocalAccessToken(data.token);
        window.location.href = "/lobby";//Página principal
      })
      .catch((error) => {         
        setMessage(error);
      });            
  }
  
    return (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center'
      }}>
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}

        <h1>Inicio de sesión</h1>

        <div>
          <FormGenerator
            ref={loginFormRef}
            inputs={loginFormInputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            listenEnterKey
            buttonText="Iniciar sesión"
            buttonClassName="auth-button"
          />
        </div>
        <div style={{
          padding: '50px'
        }}>
          ¿No tienes una cuenta? <a href="register">Regístrate</a>
        </div>
      </div>
    );  
}

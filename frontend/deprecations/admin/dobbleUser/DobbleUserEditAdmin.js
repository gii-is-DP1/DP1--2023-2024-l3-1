import { useState } from "react";
import { Link } from "react-router-dom";
import {
  Form,
  Input,
  Label,
  Row
} from "reactstrap";
import tokenService from "../../../src/services/token.service";
import getErrorModal from "../../../src/util/getErrorModal";
import getIdFromUrl from "../../../src/util/getIdFromUrl";
import useFetchData from "../../../src/util/useFetchData";
import useFetchState from "../../../src/util/useFetchState";

const jwt = tokenService.localAccessToken;

export default function DobbleUserEditAdmin() {
  const emptyItem = {
    id: "",
    email: "",
    username: "",
    authority: 5
  };
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [dobbleUser, setDobbleUser] = useFetchState(
    emptyItem,
    `/api/v1/dobbleUsers/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const users = useFetchData(`/api/v1/users`, jwt);

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "user") {
      const user = users.find((u) => u.id === Number(value));
      setDobbleUser({ ...dobbleUser, user: user });
    } else 
        setDobbleUser({ ...dobbleUser, [name]: value });
  }



  function handleSubmit(event) {
    event.preventDefault();

    fetch("/api/v1/dobbleUsers" + (dobbleUser.id ? "/" + dobbleUser.id : ""), {
      method: dobbleUser.id ? "PUT" : "POST",
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
        } else window.location.href = "/dobbleUsers";
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);

  

  return (
    <div className="auth-page-container">
      {<h2>{dobbleUser.id ? "Edit DobbleUser" : "Add DobbleUser"}</h2>}
      {modal}
      <div className="auth-form-container">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="email" className="custom-form-input-label">
              Email
            </Label>
            <Input
              type="text"
              required
              name="email"
              id="email"
              value={dobbleUser.email || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
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
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-button-row">
            <button className="auth-button">Save</button>
            <Link
              to={`/dobbleUsers`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Cancel
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}

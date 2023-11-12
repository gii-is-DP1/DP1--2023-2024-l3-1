import { useState } from "react";
import { Link } from "react-router-dom";
import {
  Form,
  Input,
  Label,
  Row
} from "reactstrap";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function DobbleUserEditAdmin() {
  const emptyItem = {
    id: "",
    email: "",
    username: "",
    user: {},
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
    } else setDobbleUser({ ...dobbleUser, [name]: value });
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

  
  const userOptions = users.map((user) => (
    <option key={user.id} value={user.id}>
      {user.username}
    </option>
  ));

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
              name="username"
              id="username"
              value={dobbleUser.username || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
         
      
        
          <div className="custom-form-input">
            {dobbleUser.id ? (
              <Input
                type="select"
                disabled
                name="user"
                id="user"
                value={dobbleUser.user?.id || ""}
                onChange={handleChange}
                className="custom-input"
              >
                <option value="">None</option>
                {userOptions}
              </Input>
            ) : (
              <Input
                type="select"
                required
                name="user"
                id="user"
                value={dobbleUser.user?.id || ""}
                onChange={handleChange}
                className="custom-input"
              >
                <option value="">None</option>
                {userOptions}
              </Input>
            )}
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
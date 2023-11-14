import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.localAccessToken;

export default function DobbleUserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [dobbleUsers, setDobbleUsers] = useFetchState(
    [],
    `/api/v1/dobbleUsers`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);

  const dobbleUserList = dobbleUsers.map((dobbleUser) => {
    return (
      <tr key={dobbleUser.id}>
        <td>{dobbleUser.username}</td>
        <td>{dobbleUser.email}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              aria-label={"edit-" + dobbleUser.user.username}
              color="primary"
              tag={Link}
              to={"/dobbleUsers/" + dobbleUser.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              aria-label={"delete-" + dobbleUser.user.username}
              color="danger"
              onClick={() =>
                deleteFromList(
                  `/api/v1/dobbleUsers/${dobbleUser.id}`,
                  dobbleUser.id,
                  [dobbleUsers, setDobbleUsers],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                )
              }
            >
              Delete
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">DobbleUsers</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <div className="float-right">
        <Button color="success" tag={Link} to="/dobbleUsers/new">
          Add DobbleUser
        </Button>{" "}
      </div>
      <div>
        <Table aria-label="dobbleUsers" className="mt-4">
          <thead>
            <tr>
              <th width="15%">Username</th>
              <th width="15%">Email</th>
              <th width="20%">Actions</th>
            </tr>
          </thead>
          <tbody>{dobbleUserList}</tbody>
        </Table>
      </div>
    </div>
  );
}

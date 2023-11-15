import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Table } from "reactstrap";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import axios from '../../services/api';

export default function PlayerListAdmin() {
  const [message, setMessage] = useState(null);
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(null);
  const [visible, setVisible] = useState(false);

  async function request() {
    try {
      setLoading(true);
      setMessage(null);

      const response = await axios("/api/v1/player", {
        method: "GET",
      });

      if (response.status === 401) {
        setMessage("Usuario actual no es administrador");
        return;
      } else if (response.status >= 500) {
        setMessage("Error del servidor");
        return;
      }

      setPlayers(await response.json());
    } catch (e) {
      setMessage(String(e));
    } finally {
      setLoading(false);
    }     
  }

  useEffect(() => {
    const run = async () => {
      await request();
    }
    run();
  }, []);

  const [alerts, setAlerts] = useState([]);

  const playerList = players.map((player) => {
    return (
      <tr key={player.id}>
        <td>{player.profile_icon}</td>
        <td>{player.username}</td>
        <td>{player.email}</td>
        <td>{player.is_admin?'ADMIN':'PLAYER'}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + player.id}
              tag={Link}
              to={"/player/" + player.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + player.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/player/${player.id}`,
                  player.id,
                  [players, setPlayers],
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
    <div className="home-page-container">
      <div className="admin-page-container">
        <h1 className="text-center">Players</h1>
        {alerts.map((a) => a.alert)}
        {modal}
        <Button color="success" tag={Link} to="/users/new">
          Add Player
        </Button>
        <div>
          <Table aria-label="player" className="mt-4">
            <thead>
              <tr>
                <th>Profile Icon</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>{playerList}</tbody>
          </Table>
        </div>
      </div>
    </div>
  );
}

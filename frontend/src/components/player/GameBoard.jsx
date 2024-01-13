import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Badge } from "reactstrap";
import axios from '../../services/api';
import DIcon from "../ui/DIcon";
import Card from "./DobbleCard";
import UserAvatar from "./UserAvatar";

const leftSizeMatchings = {
  0: {
    left: -8,
    top: -2,
    scale: 1.8
  },
  1: {
    left: -8,
    top: 0,
    scale: 1.8
  },
  2: {
    left: -8,
    top: 2,
    scale: 1.8
  }
}

const rightSizeMatchings = {
  0: {
    left: 8,
    top: -2,
    scale: 1.8
  },
  1: {
    left: 8,
    top: -1,
    scale: 1.8
  },
  2: {
    left: 8,
    top: 0,
    scale: 1.8
  },
  3: {
    left: 8,
    top: 1,
    scale: 1.8
  }
}

/**
 * Este es el componente que muestra el tablero de juego. Recibe como Props los datos de la partida
 * Solo los botones de las cartas del jugador pueden interactuar con la API, el resto de elementos seguirá una comunicación
 * unidireccional padre -> hijo.
 */
export default function GameBoard(props) {
  const [striked, setStriked] = useState();
  const user = useSelector(state => state.appStore.user);
  const playersCards = () => props.game.game_players.filter((p) => p.username !== user.username);
  const currentPlayer = () => props.game.game_players.filter((p) => p.username === user.username)[0];

  /**
   * TODO: Preparar la página para el modo espectador
   */
  const middle = Math.ceil(!currentPlayer() ? (playersCards().length / 2) : ((playersCards().length - 1) / 2));
  const right_side = playersCards().slice(0, middle);
  const left_side = playersCards().slice(middle);

  async function playFigure(icon) {
    try {
      await axios.post(`/games/me/play`, { icon });
    } catch (e) {
      if (e.response?.status === 417) {
        setStriked(icon);
      }
    }
  }

  useEffect(() => {
    const timeout = window.setTimeout(() => {
      setStriked();
    }, 1000);

    return () => {
      clearInterval(timeout);
    }
  }, [striked]);

  return (
  <>
    <div style={{
      display: 'grid',
      gridTemplateColumns: '1fr', 
      gridTemplateRows: '1.9fr 0.1fr', 
      gap: '5vh 0', 
      gridTemplateAreas: `
        "main"
        "footer"
      `
    }}>
      <div className="main" style={{ 
        display: 'grid',
        gridTemplateColumns: '0.6fr 1.4fr 0.6fr',
        gridTemplateRows: '0.2fr 0.2fr 0.2fr 0.3fr',
        gap: '0 0',
        gridTemplateAreas: `
          "left-players card right-players"
          "left-players card right-players"
          "left-players card right-players"
          ". player-card ."
        `
      }}>
        <div style={{ 
          gridArea: 'left-players',
          justifySelf: 'end'
        }}>
          {left_side.map((game_player, i) => {
            return (
              <>
                <div>
                  <UserAvatar
                    user={game_player}
                    size="small"
                    style={{
                      margin: '0',
                      backgroundColor: 'var(--button-active)'
                  }} />
                  <Badge pill color="primary">{game_player.username}</Badge>
                  
                </div>
                <Card
                  card={game_player.current_card ?? {}}
                  style={{
                    width: `5vw`,
                    height: `5vw`,
                    position: 'relative',
                    left: `${leftSizeMatchings[i].left}vw`,
                    top: `${leftSizeMatchings[i].top}vw`,
                    transform: `scale(${leftSizeMatchings[i].scale})`
                  }}/>
              </>
            );
          })}
        </div>
        <div style={{ 
          gridArea: 'card',
          justifySelf: 'center',
        }}>
          <Card
            card={props.game.central_card ?? {}}
            style={{
              width: '30vw',
              height: '30vw',
              position: 'relative',
              left: '6vw'
            }}
          />
        </div>
        <div style={{ 
          gridArea: 'player-card',
          position: 'relative',
          left: '20vw',
          top: '5vw',
          transform: 'scale(1.7)'
        }}>
          {/* Con undefined se pone automáticamente el avatar del usuario actual */}
          <UserAvatar
            user={currentPlayer() ? undefined : playersCards().at(-1).player}
            size="small"
            style={{ 
              margin: '0',
              backgroundColor: 'var(--button-active)'
          }}>
            <Badge pill color="primary">{currentPlayer() ? <>Tú</> : playersCards().at(-1).player.username}</Badge>
          </UserAvatar>
          <Card
            card={currentPlayer() ? currentPlayer().current_card ?? {} : playersCards().at(-1).current_card ?? {}}
            style={{
              width: '11vw',
              height: '11vw',
          }}/>
        </div>
        <div style={{ 
          gridArea: 'right-players',
          justifySelf: 'start'
        }}>
          {right_side.map((game_player, i) => {
            return (
              <>
                <div>
                  <UserAvatar
                    user={game_player}
                    size="small"
                    style={{
                      margin: '0',
                      backgroundColor: 'var(--button-active)'
                  }} />
                  <Badge pill color="primary">{game_player.username}</Badge>
                  
                </div>
                <Card
                  card={game_player.current_card ?? {}}
                  style={{
                    width: `5vw`,
                    height: `5vw`,
                    position: 'relative',
                    left: `${rightSizeMatchings[i].left}vw`,
                    top: `${rightSizeMatchings[i].top}vw`,
                    transform: `scale(${rightSizeMatchings[i].scale})`
                  }}/>
              </>
            );
          })}
        </div>
      </div>
      {currentPlayer() ? 
      <>
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          gridArea: 'footer'
        }}>
          {(currentPlayer().current_card?.figures ?? []).map((f) => {
            return (
            <>
              <DIcon
                icon={f.icon}
                onClick={() => playFigure(f.icon)}
                style={{
                  backgroundColor: striked === f.icon ? 'red' : 'white',
                  width: '8vw',
                  height: '8vw',
                  pointerEvents: 'all',
                  cursor: 'pointer'
                }} />
            </>)
          })}
        </div>
      </>
      : undefined}
    </div>
  </>);
}

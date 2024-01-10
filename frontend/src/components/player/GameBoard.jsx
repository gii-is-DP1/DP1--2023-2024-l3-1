import { useEffect } from "react";
import { Badge } from "reactstrap";
import { appStore } from "../../services/appStore";
import DIcon from "../ui/DIcon";
import Card from "./DobbleCard";
import UserAvatar from "./UserAvatar";

const leftSizeMatchings = {
  0: {
    left: 12,
    top: -3.5,
    scale: 1.8
  },
  1: {
    left: 8,
    top: -2.5,
    scale: 1.8
  },
  2: {
    left: -5,
    top: 2,
    scale: 1.8
  }
}

const rightSizeMatchings = {
  0: {
    left: -10,
    top: -4,
    scale: 1.8
  },
  1: {
    left: -9,
    top: -1,
    scale: 1.8
  },
  2: {
    left: -10,
    top: 1,
    scale: 1.8
  },
  3: {
    left: -18,
    top: -1,
    scale: 1.8
  }
}

/**
 * Este es el componente que muestra el tablero de juego. Recibe como Props los datos de la partida
 * Solo los botones de las cartas del jugador pueden interactuar con la API, el resto de elementos seguirá una comunicación
 * unidireccional padre -> hijo.
 */
export default function GameBoard(props) {
  useEffect(() => {
    appStore.dispatch({ type: 'appStore/setNavbar', payload: { name: 'GAME', content: 'Test' }});

    return () => {
      appStore.dispatch({ type: 'appStore/setNavbar', payload: undefined });
    }
  }, []);

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
          {Array.from({ length: 3 }).map((_e, i) => {
            return (
              <>
                <div>
                  <UserAvatar size="small" style={{
                    margin: '0',
                    backgroundColor: 'var(--button-active)'
                  }} />
                  <Badge pill color="primary">Username</Badge>
                  
                </div>
                <Card style={{
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
            style={{
              width: '30vw',
              height: '30vw'
            }}
          />
        </div>
        <div style={{ 
          gridArea: 'player-card',
          position: 'relative',
          left: '20%',
          top: '50%',
          transform: 'scale(1.4)'
        }}>
          <UserAvatar size="small" style={{ 
            margin: '0',
            backgroundColor: 'var(--button-active)'
          }}>
            <Badge pill color="primary">Tú</Badge>
          </UserAvatar>
          <Card
            style={{
              width: '11vw',
              height: '11vw',
          }}/>
        </div>
        <div style={{ 
          gridArea: 'right-players',
          justifySelf: 'start'
        }}>
            {Array.from({ length: 4 }).map((_e, i) => {
            return (
              <>
                <div>
                  <UserAvatar size="small" style={{
                    margin: '0',
                    backgroundColor: 'var(--button-active)'
                  }} />
                  <Badge pill color="primary">Username</Badge>
                  
                </div>
                <Card style={{
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
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        gridArea: 'footer'
      }}>
        {Array.from({ length: 8 }).map((i) => {
          return <DIcon icon="DELFIN" style={{
            backgroundColor: 'white',
            width: '8vw',
            height: '8vw'
          }} />;
        })}
      </div>
    </div>
  </>);
}

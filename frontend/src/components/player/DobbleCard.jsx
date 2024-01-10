import { Icon } from "../../models/enums";
import DIcon from "../ui/DIcon";

const parentStyles = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  borderRadius: '50%',
  background: 'white',
  overflow: 'hidden',
}

const childBox = {
  width: '75%',
  height: '66%',
}

const innerGrid = {
  width: '100%',
  height: '100%',
  display: 'grid',
  position: 'relative',
  gridAutoRows: '1fr',
  gridTemplateColumns: '1fr, 1fr, 1fr',
  gridTemplateRows: '1fr, 1fr, 1fr',
  gap: '2% 2%',
  gridTemplateAreas: `
    "fig-1 fig-2 fig-3"
    "fig-4 fig-5 fig-6"
    "fig-7 fig-8 fig-9"
  `
}

const cellStyles = {
  position: 'relative',
  width: '100%',
  height: '100%',
}

const indexPos = {
  '0': {
    topMax: '0',
    topMin: '0',
    leftMax: '0',
    leftMin: '0'
  },
  '1': {
    topMax: '-40',
    topMin: '-70',
    leftMax: '7',
    leftMin: '-7'
  },
  '2': {
    topMax: '0',
    topMin: '0',
    leftMax: '0',
    leftMin: '0'
  },
  '3': {
    topMax: '0',
    topMin: '0',
    leftMax: '-35',
    leftMin: '-48'
  },
  '4': {
    topMax: '40',
    topMin: '-40',
    leftMax: '40',
    leftMin: '-40'
  },
  '5': {
    topMax: '70',
    topMin: '20',
    leftMax: '40',
    leftMin: '20'
  },
  '6': {
    topMax: '40',
    topMin: '20',
    leftMax: '12',
    leftMin: '5'
  },
  '7': {
    topMax: '70',
    topMin: '10',
    leftMax: '40',
    leftMin: '15'
  }
}

const scales = {
  'x-small': 'scale(0.6)',
  'small': 'scale(0.7)',
  'medium': 'scale(0.75)',
  'large': 'scale(0.8)',
  'max': 'scale(0.9)'
}

export default function Card(props) {
  const iconArray = Object.keys(Icon);
  const scalesArray = Object.keys(scales);
  const scalesLength = scalesArray.length;
  const iconLength = iconArray.length;

  function randomBetween(min, max) {
    if (min > max) {
      [min, max] = [max, min];
    }

    if (min === max) {
      return min;
    }

    return ~~(Math.random() * Math.abs(max - min) + 1) + Math.min(min, max);
  }

  return (
    <>
      <div style={{ ...props.style, ...parentStyles}}>
        <div style={childBox}>
          <div style={innerGrid}>
            {Array.from({ length: 8 }).map((i, index) => {
              const randElement = iconArray[~~(Math.random() * iconLength)];
              const randScale = scalesArray[~~(Math.random() * scalesLength)];
              const rotation = Math.random() * 360;
              const top = randomBetween(indexPos[index].topMin, indexPos[index].topMax);
              const left = randomBetween(indexPos[index].leftMin, indexPos[index].leftMax);

              return (
                <div key={index+1}
                style={{ 
                  gridArea: `fig-${index+1}`,
                  top: `${top}%`,
                  left: `${left}%`,
                  ...cellStyles 
                  }}>
                  <DIcon
                  rotation={rotation}
                  icon={randElement}
                  style={{
                    overflow: 'hidden',
                    transform: scales[randScale],
                }} />
              </div>
            )
          })}
          </div>
        </div>
      </div>
    </>
  )
}

import React from 'react';
// Importación de las imágenes de los iconos
import DELFIN from '../../static/images/card_icons/dolphin.webp';
import TIJERAS from '../../static/images/card_icons/scissors.webp';
import SNOWMAN from '../../static/images/card_icons/snowman.webp';
import RAYO from '../../static/images/card_icons/lightning-bolt.webp';
import FANTASMA from '../../static/images/card_icons/ghost.webp';
import OBJETIVO from '../../static/images/card_icons/target.webp';
import GAFAS from '../../static/images/card_icons/sunglasses.webp';
import EXCLAMACION from '../../static/images/card_icons/exclamation-point.webp';
import MARTILLO from '../../static/images/card_icons/hammer.webp';
import CACTUS from '../../static/images/card_icons/cactus.webp';
import CEBRA from '../../static/images/card_icons/zebra.webp';
import LAPIZ from '../../static/images/card_icons/pencil.webp';
import MANZANA from '../../static/images/card_icons/apple.webp';
import TORTUGA from '../../static/images/card_icons/tortoise.webp';
import GATO from '../../static/images/card_icons/cat.webp';
import BIBERON from '../../static/images/card_icons/baby-bottle.webp';
import SPIDER from '../../static/images/card_icons/spider.webp';
import MARIQUITA from '../../static/images/card_icons/ladybug.webp';
import YIN_YANG from '../../static/images/card_icons/yin-and-yang.webp';
import INTERROGACION from '../../static/images/card_icons/question-mark.webp';
import IGLU from '../../static/images/card_icons/igloo.webp';
import TREBOL from '../../static/images/card_icons/four-leaf-clover.webp';
import QUESO from '../../static/images/card_icons/cheese.webp';
import LLAVE from '../../static/images/card_icons/key.webp';
import OJO from '../../static/images/card_icons/eye.webp';
import CLAVE_SOL from '../../static/images/card_icons/treble-clef.webp';
import PERRO from '../../static/images/card_icons/dog.webp';
import CORAZON from '../../static/images/card_icons/heart.webp';
import PAYASO from '../../static/images/card_icons/clown.webp';
import BUHO from '../../static/images/card_icons/bird.webp';
import AGUA from '../../static/images/card_icons/water-drip.webp';
import PINTURA from '../../static/images/card_icons/green-splats.webp';
import ANCLA from '../../static/images/card_icons/anchor.webp';
import BOMBA from '../../static/images/card_icons/bomb.webp';
import HIELO from '../../static/images/card_icons/ice-cube.webp';
import DINOSAURIO from '../../static/images/card_icons/dinosaur.webp';
import CABALLO_AJEDREZ from '../../static/images/card_icons/chess-knight.webp';
import COPO_NIEVE from '../../static/images/card_icons/snowflake.webp';
import BOMBILLA from '../../static/images/card_icons/light-bulb.webp';
import VELA from '../../static/images/card_icons/candle.webp';
import COCHE from '../../static/images/card_icons/taxi.webp';
import CANDADO from '../../static/images/card_icons/lock.webp';
import SOL from '../../static/images/card_icons/sun.webp';
import LUNA from '../../static/images/card_icons/moon.webp';
import CALAVERA from '../../static/images/card_icons/skull-and-crossbones.webp';
import RELOJ from '../../static/images/card_icons/clock.webp';
import ZANAHORIA from '../../static/images/card_icons/carrot.webp';
import HOJA_CADUCA from '../../static/images/card_icons/maple-leaf.webp';
import TELARANA from '../../static/images/card_icons/spider-web.webp';
import MUNECO from '../../static/images/card_icons/orange-scarecrow-man.webp';
import DRAGON from '../../static/images/card_icons/dragon.webp';
import FUEGO from '../../static/images/card_icons/fire.webp';
import MANO_LOGO from '../../static/images/card_icons/dobble-hand.webp';
import FLOR from '../../static/images/card_icons/daisy-flower.webp';
import LABIOS from '../../static/images/card_icons/red-lips.webp';
import ARBOL from '../../static/images/card_icons/tree.webp';
import PROHIBIDO from '../../static/images/card_icons/no-entry-sign.webp';

const iconMapping = Object.freeze({
  DELFIN,
  TIJERAS,
  SNOWMAN,
  RAYO,
  FANTASMA,
  OBJETIVO,
  GAFAS,
  EXCLAMACION,
  MARTILLO,
  CACTUS,
  CEBRA,
  LAPIZ,
  MANZANA,
  TORTUGA,
  GATO,
  BIBERON,
  SPIDER,
  MARIQUITA,
  YIN_YANG,
  INTERROGACION,
  IGLU,
  TREBOL,
  QUESO,
  LLAVE,
  OJO,
  CLAVE_SOL,
  PERRO,
  CORAZON,
  PAYASO,
  BUHO,
  AGUA,
  PINTURA,
  ANCLA,
  BOMBA,
  HIELO,
  DINOSAURIO,
  CABALLO_AJEDREZ,
  COPO_NIEVE,
  BOMBILLA,
  VELA,
  COCHE,
  CANDADO,
  SOL,
  LUNA,
  CALAVERA,
  RELOJ,
  ZANAHORIA,
  HOJA_CADUCA,
  TELARANA,
  MUNECO,
  DRAGON,
  FUEGO,
  MANO_LOGO,
  FLOR,
  LABIOS,
  ARBOL,
  PROHIBIDO
});

export default function DIcon(Props) {
    return (
        <div {...Props}
        style={{
            backgroundImage: `url("${Props.icon ? iconMapping[Props.icon] : iconMapping.MANO_LOGO}")`,
            backgroundRepeat: 'no-repeat',
            backgroundSize: 'contain',
            backgroundColor: 'white',
            backgroundPosition: 'center center',
            width: '100%',
            height: '100%',
            borderRadius: '100%',
            overflow: 'hidden',
            ...(Props.disabled && { filter: 'grayscale(100%)' }),
            ...(Props.rotation && { transform: `rotate(${Props.rotation}deg)` }),
            ...Props.style,
        }}>
            {Props.children}
        </div>
    );
}

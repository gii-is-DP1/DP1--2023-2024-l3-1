import React from 'react';
// Importación de las imágenes de los iconos
import ANCLA from '../../static/images/card_icons/anchor.webp';
import MANZANA from '../../static/images/card_icons/apple.webp';
import BIBERON from '../../static/images/card_icons/baby-bottle.webp';
import BUHO from '../../static/images/card_icons/bird.webp';
import BOMBA from '../../static/images/card_icons/bomb.webp';
import CACTUS from '../../static/images/card_icons/cactus.webp';
import VELA from '../../static/images/card_icons/candle.webp';
import ZANAHORIA from '../../static/images/card_icons/carrot.webp';
import GATO from '../../static/images/card_icons/cat.webp';
import QUESO from '../../static/images/card_icons/cheese.webp';
import CABALLO_AJEDREZ from '../../static/images/card_icons/chess-knight.webp';
import RELOJ from '../../static/images/card_icons/clock.webp';
import PAYASO from '../../static/images/card_icons/clown.webp';
import FLOR from '../../static/images/card_icons/daisy-flower.webp';
import DINOSAURIO from '../../static/images/card_icons/dinosaur.webp';
import MANO_LOGO from '../../static/images/card_icons/dobble-hand.webp';
import PERRO from '../../static/images/card_icons/dog.webp';
import DELFIN from '../../static/images/card_icons/dolphin.webp';
import DRAGON from '../../static/images/card_icons/dragon.webp';
import EXCLAMACION from '../../static/images/card_icons/exclamation-point.webp';
import OJO from '../../static/images/card_icons/eye.webp';
import FUEGO from '../../static/images/card_icons/fire.webp';
import TREBOL from '../../static/images/card_icons/four-leaf-clover.webp';
import FANTASMA from '../../static/images/card_icons/ghost.webp';
import PINTURA from '../../static/images/card_icons/green-splats.webp';
import MARTILLO from '../../static/images/card_icons/hammer.webp';
import CORAZON from '../../static/images/card_icons/heart.webp';
import HIELO from '../../static/images/card_icons/ice-cube.webp';
import IGLU from '../../static/images/card_icons/igloo.webp';
import LLAVE from '../../static/images/card_icons/key.webp';
import MARIQUITA from '../../static/images/card_icons/ladybug.webp';
import BOMBILLA from '../../static/images/card_icons/light-bulb.webp';
import RAYO from '../../static/images/card_icons/lightning-bolt.webp';
import CANDADO from '../../static/images/card_icons/lock.webp';
import HOJA_CADUCA from '../../static/images/card_icons/maple-leaf.webp';
import LUNA from '../../static/images/card_icons/moon.webp';
import PROHIBIDO from '../../static/images/card_icons/no-entry-sign.webp';
import MUNECO from '../../static/images/card_icons/orange-scarecrow-man.webp';
import LAPIZ from '../../static/images/card_icons/pencil.webp';
import INTERROGACION from '../../static/images/card_icons/question-mark.webp';
import LABIOS from '../../static/images/card_icons/red-lips.webp';
import TIJERAS from '../../static/images/card_icons/scissors.webp';
import CALAVERA from '../../static/images/card_icons/skull-and-crossbones.webp';
import COPO_NIEVE from '../../static/images/card_icons/snowflake.webp';
import SNOWMAN from '../../static/images/card_icons/snowman.webp';
import TELARANA from '../../static/images/card_icons/spider-web.webp';
import SPIDER from '../../static/images/card_icons/spider.webp';
import SOL from '../../static/images/card_icons/sun.webp';
import GAFAS from '../../static/images/card_icons/sunglasses.webp';
import OBJETIVO from '../../static/images/card_icons/target.webp';
import COCHE from '../../static/images/card_icons/taxi.webp';
import TORTUGA from '../../static/images/card_icons/tortoise.webp';
import CLAVE_SOL from '../../static/images/card_icons/treble-clef.webp';
import ARBOL from '../../static/images/card_icons/tree.webp';
import AGUA from '../../static/images/card_icons/water-drip.webp';
import YIN_YANG from '../../static/images/card_icons/yin-and-yang.webp';
import CEBRA from '../../static/images/card_icons/zebra.webp';

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
            backgroundPosition: 'center center',
            width: '100%',
            height: '100%',
            borderRadius: '100%',
            overflow: 'hidden',
            ...(Props.disabled && { filter: 'grayscale(100%)' }),
            ...(Props.rotation && { rotate: `${Props.rotation}deg` }),
            ...Props.style,
        }} />
    );
}

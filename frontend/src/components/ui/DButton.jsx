import React from 'react';
import { inputStyles } from './styles/forms';

/**
 * 
 * @param {*} Props[color] - Tamaños posibles: ``yellow``, ``red``, ``green``, ``white`` o sin establecer (morado por defecto)
 * @returns 
 */
export default function DButton(Props) {
    const getBackgroundColor = () => {
        switch (Props.color) {
            case 'yellow':
                return '#FFD700';
            case 'red':
                return '#FF3300';
            case 'green':
                return '#0ED222';
            case 'white':
                return 'white';
            default:
                return '#61196C';
        }
    }

    const getTextColor = () => {
        switch (Props.color) {
            case 'yellow':
            case 'red':
            case 'white':
                return 'black';
            default:
                return 'white';
        }
    }
    return (
        <button {...Props}
        onClick={!Props.disabled ? Props.onClick : undefined}
        style={{
            ...inputStyles,
            backgroundColor: `${getBackgroundColor()}`,
            color: `${getTextColor()}`,
            ...(Props.disabled && { filter: 'grayscale(100%)', pointerEvents: 'none' }),
            ...Props.style,
        }}>
            {Props.children}
        </button>
    );
}

import React from 'react';
import { inputStyles } from './styles/forms';

export default function DButton(Props) {
    return (
        <button {...Props}
        style={{
            ...inputStyles,
            backgroundColor: '#61196C',
            color: 'white',
            ...Props.style,
        }}>
            {Props.children}
        </button>
    );
}

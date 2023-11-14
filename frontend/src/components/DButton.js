import React from 'react';
import { inputStyles } from './sharedStyles';

export default function DButton(Props) {
    return (
        <button {...Props}
        style={{
            ...inputStyles,
            ...Props.style,
            backgroundColor: '#61196C',
            color: 'white',
        }}>
            {Props.text}
        </button>
    );
}

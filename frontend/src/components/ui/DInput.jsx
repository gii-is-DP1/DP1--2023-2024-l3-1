import React from 'react';
import { inputStyles } from './styles/forms';

export default function DInput(props) {
    return String(props.type).toLowerCase() !== 'select' ? (
        <input {...props}
        style={{
            ...inputStyles,
            ...props.style
        }}>
            {props.children}
        </input>
    ) : (
        <select {...props}
        style={{
            ...inputStyles,
            width: '19rem',
            ...props.style
        }}>
            {props.children}
        </select>
    );
}

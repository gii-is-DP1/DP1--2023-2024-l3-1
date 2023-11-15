import React from 'react';
import { inputStyles } from './styles/forms';

export default function DInput(props) {
    return (
        <input {...props}
        style={{
            ...inputStyles,
            ...props.style
        }}>
            {props.children}
        </input>
    );
}

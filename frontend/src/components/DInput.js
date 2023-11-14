import React from 'react';
import { inputStyles } from './sharedStyles';

export default function DInput(props) {
    return (
        <input {...props}
        style={{
            ...inputStyles,
            ...props.style
        }} />
    );
}

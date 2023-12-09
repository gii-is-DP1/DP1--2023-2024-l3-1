import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import '@fontsource/lakki-reddy';
import App from './App';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { tokenStore } from './services/token.service';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Provider store={tokenStore}>
        <App />
      </Provider>
    </BrowserRouter >
  </React.StrictMode>
);

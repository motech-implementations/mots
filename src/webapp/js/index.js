import ReactDOM from 'react-dom';
import React from 'react';
import { Provider } from 'react-redux';
import { HashRouter, Route, Switch } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';

import 'react-toastify/dist/ReactToastify.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import '../css/main.scss';

import App from './components/app';
import Signin from './components/auth/signin';
import requireAuth from './components/auth/require_auth';
import authenticateToken from './components/auth/authenticate_token';

import Store from './store';

// eslint-disable-next-line import/prefer-default-export
export const { dispatch } = Store;

authenticateToken();

ReactDOM.render(
  <Provider store={Store}>
    <HashRouter>
      <div>
        <Switch>
          <Route path="/login" component={Signin} />
          <Route path="/" component={requireAuth(App)} />
        </Switch>
        <ToastContainer
          autoClose={false}
          limit={3}
        />
      </div>
    </HashRouter>
  </Provider>,
  document.getElementById('root'),
);

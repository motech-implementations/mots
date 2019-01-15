import ReactDOM from 'react-dom';
import React from 'react';
import { Provider } from 'react-redux';
import { HashRouter, Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';

import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/bouncyflip.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import '../css/main.scss';

import App from './components/app';
import Signin from './components/auth/signin';
import requireAuth from './components/auth/require_auth';
import authenticateToken from './components/auth/authenticate_token';

import Store from './store';
import Registration from './components/registration';

// eslint-disable-next-line import/prefer-default-export
export const { dispatch } = Store;

authenticateToken();

ReactDOM.render(
  <Provider store={Store}>
    <HashRouter>
      <div>
        <Switch>
          <Route path="/login" component={Signin} />
          <Route path="/register/:token" component={Registration} />
          <Route path="/" component={requireAuth(App)} />
        </Switch>
        <Alert
          timeout="none"
          stack={{ limit: 3 }}
          offset={29.5}
          html
          position="top-right"
          effect="bouncyflip"
        />
      </div>
    </HashRouter>
  </Provider>
  , document.getElementById('root'),
);

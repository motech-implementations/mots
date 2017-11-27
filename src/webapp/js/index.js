import ReactDOM from 'react-dom';
import React from 'react';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { HashRouter, Route, Switch } from 'react-router-dom';
import reduxThunk from 'redux-thunk';
import ReduxPromise from 'redux-promise';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import '../css/main.scss';

import reducers from './reducers';
import App from './components/app';
import Signin from './components/auth/signin';
import requireAuth from './components/auth/require_auth';
import { AUTH_USER } from './actions/types';

const createStoreWithMiddleware = applyMiddleware(reduxThunk, ReduxPromise)(createStore);
const store = createStoreWithMiddleware(reducers);

const token = localStorage.getItem('token');

if (token) {
  store.dispatch({ type: AUTH_USER });
}

ReactDOM.render(
  <Provider store={store}>
    <HashRouter>
      <div>
        <Switch>
          <Route path="/login" component={Signin} />
          <Route path="/" component={requireAuth(App)} />
        </Switch>
      </div>
    </HashRouter>
  </Provider>
  , document.getElementById('root'),
);

import React from 'react';
import { Provider } from 'react-redux';

import {
  Scene,
  Router,
} from 'react-native-router-flux';
import Sentry from 'sentry-expo';

import Header from './components/Header';
import Home from './components/Home';
import Incharges from './components/Incharges';
import HealthWorkers from './components/HealthWorkers';
import Store from './store';
import AppDrawer from './components/AppDrawer';
import Login from './container/Login';
import Config from './config';

Sentry.config(Config.sentryConfig.publicDSN).install();

export const { dispatch } = Store;

const App = () => (
  <Provider store={Store}>
    <Router>
      <Scene key="auth" initial>
        <Scene key="login" component={Login} initial hideNavBar />
      </Scene>
      <Scene key="drawer" component={AppDrawer} open={false}>
        <Scene key="main">
          <Scene key="home" component={Home} title="Home" hideNavBar={false} navBar={Header} initial />
          <Scene key="incharges" component={Incharges} title="Incharge List" hideNavBar={false} navBar={Header} />
          <Scene key="chws" component={HealthWorkers} title="Community Health Workers" navBar={Header} />
        </Scene>
      </Scene>
    </Router>
  </Provider>
);

export default App;

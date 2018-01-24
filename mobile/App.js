import React from 'react';
import { Provider } from 'react-redux';

import {
  Scene,
  Router,
} from 'react-native-router-flux';

import Header from './components/Header';
import Home from './components/Home';
import Incharges from './components/Incharges';
import HealthWorkers from './components/HealthWorkers';

import Store from './store';
import AppDrawer from './components/AppDrawer';

export const { dispatch } = Store;

const App = () => (
  <Provider store={Store}>
    <Router>
      <Scene key="drawer" component={AppDrawer} open={false}>
        <Scene key="main">
          <Scene key="home" component={Home} title="Home" navBar={Header} initial />
          <Scene key="incharges" component={Incharges} title="Incharge List" navBar={Header} />
          <Scene key="chws" component={HealthWorkers} title="Community Health Workers" navBar={Header} />
        </Scene>
      </Scene>
    </Router>
  </Provider>
);

export default App;

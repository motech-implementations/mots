import React from 'react';
import { Provider } from 'react-redux';

import {
  Scene,
  Router,
  Stack,
} from 'react-native-router-flux';

import Header from './components/Header';
import Home from './components/Home';
import Menu from './components/Menu';
import Incharges from './components/Incharges';

import Store from './store';

export const { dispatch } = Store;

const App = () => (
  <Provider store={Store}>
    <Router>
      <Stack key="root">
        <Scene key="home" component={Home} title="Home" navBar={Header} />
        <Scene key="menu" component={Menu} title="Menu" navBar={Header} />
        <Scene key="incharges" component={Incharges} title="Incharge List" navBar={Header} />
      </Stack>
    </Router>
  </Provider>
);

export default App;

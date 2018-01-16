import React from 'react';
import { Provider } from 'react-redux';

import {
  Scene,
  Router,
  Stack,
} from 'react-native-router-flux';

import Home from './components/Home';

import Store from './store';

const App = () => (
  <Provider store={Store}>
    <Router>
      <Stack key="root">
        <Scene key="login" component={Home} title="Home Page" />
      </Stack>
    </Router>
  </Provider>
);

export default App;

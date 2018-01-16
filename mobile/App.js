import React from 'react';

import {
  Scene,
  Router,
  Stack,
} from 'react-native-router-flux';

import Home from './components/Home';

const App = () => (
  <Router>
    <Stack key="root">
      <Scene key="login" component={Home} title="Home Page" />
    </Stack>
  </Router>
);

export default App;

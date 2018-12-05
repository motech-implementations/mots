import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { View, StatusBar, NetInfo } from 'react-native';

import Store from './store';
import { setConnectionState } from './actions';
import AppRouter from './components/AppRouter';

export const { dispatch } = Store;

export default class App extends Component {
  componentDidMount() {
    NetInfo.getConnectionInfo().then(connInfo => dispatch(setConnectionState(connInfo)));
    NetInfo.addEventListener(
      'connectionChange',
      connInfo => dispatch(setConnectionState(connInfo)),
    );
  }

  componentWillUnmount() {
    NetInfo.removeEventListener(
      'connectionChange',
      connInfo => dispatch(setConnectionState(connInfo)),
    );
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        <StatusBar backgroundColor="black" barStyle="light-content" />
        <Provider store={Store}>
          <AppRouter />
        </Provider>
      </View>
    );
  }
}

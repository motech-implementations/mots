import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { Platform, View, KeyboardAvoidingView, StatusBar, NetInfo } from 'react-native';

import Store from './store';
import { setConnectionState } from './actions';
import AppRouter from './components/AppRouter';
import { handleConnectivityChange, setConnection } from './utils/connection';

export const { dispatch } = Store;
const isIos = Platform.OS === 'ios';

export default class App extends Component {
  componentDidMount() {
    NetInfo.getConnectionInfo().then(connInfo => setConnection(connInfo));
    NetInfo.addEventListener(
      'connectionChange',
      connInfo => handleConnectivityChange(connInfo),
    );
  }

  componentWillUnmount() {
    NetInfo.removeEventListener(
      'connectionChange',
      connInfo => handleConnectivityChange(connInfo),
    );
  }

  render() {
    return (
      <KeyboardAvoidingView style={{ flex: 1 }} behavior={(isIos) ? 'padding' : ''}>
        <StatusBar backgroundColor="black" barStyle="light-content" />
        <Provider store={Store}>
          <AppRouter />
        </Provider>
      </KeyboardAvoidingView>
    );
  }
}

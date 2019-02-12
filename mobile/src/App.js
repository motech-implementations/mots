import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { Platform, View, KeyboardAvoidingView, StatusBar, NetInfo } from 'react-native';

import Store from './store';
import { setConnectionState } from './actions';
import AppRouter from './components/AppRouter';

export const { dispatch } = Store;
const isIos = Platform.OS === 'ios';

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
      <KeyboardAvoidingView style={{ flex: 1 }} behavior={(isIos) ? 'padding' : ''}>
        <StatusBar backgroundColor="black" barStyle="light-content" />
        <Provider store={Store}>
          <AppRouter />
        </Provider>
      </KeyboardAvoidingView>
    );
  }
}

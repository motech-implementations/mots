import React, { PureComponent } from 'react';
import {
  Platform,
  KeyboardAvoidingView,
} from 'react-native';

const isIos = Platform.OS === 'ios';

export default (WrappedComponent) => {
  return class extends PureComponent {
    render() {
      return (
        <KeyboardAvoidingView style={{ flex: 1 }} behavior={(isIos) ? 'padding' : ''}>
          <WrappedComponent {...this.props} />
        </KeyboardAvoidingView>
      );
    }
  }
};

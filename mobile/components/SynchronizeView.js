import React, { Component } from 'react';
import { Text, View } from 'react-native';
import { connect } from 'react-redux';

import Synchronizer from '../utils/synchronizer';
import store from '../store';

const styles = {
  container: {
    marginTop: 60,
  },
};

class SynchronizeView extends Component {
  componentDidMount() {
    const synchronizer = new Synchronizer(store);
    synchronizer.synchronizeChws();
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>Synchronize</Text>
      </View>
    );
  }
}

function mapStateToProps() {
  return {};
}

export default connect(mapStateToProps, { })(SynchronizeView);

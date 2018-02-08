import React from 'react';
import { View, Text, PixelRatio } from 'react-native';
import HealthWorkersList from '../container/HealthWorkersList';

import styles from '../styles/listsStyles';

const HealthWorkers = () => (
  <View style={styles.container}>
    <Text style={styles.title}>
      {PixelRatio.get() < 2 ? 'CHW List' : 'Community Health Workers'}
    </Text>
    <HealthWorkersList />
  </View>
);

export default HealthWorkers;

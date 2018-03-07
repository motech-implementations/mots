import React from 'react';
import { View, Text, PixelRatio } from 'react-native';
import HealthWorkersList from '../container/HealthWorkersList';

import styles from '../styles/listsStyles';
import getContainerStyle from '../utils/styleUtils';

const HealthWorkers = () => (
  <View style={getContainerStyle()}>
    <Text style={styles.title}>
      {PixelRatio.get() < 2 ? 'CHW List' : 'Community Health Workers'}
    </Text>
    <HealthWorkersList />
  </View>
);

export default HealthWorkers;

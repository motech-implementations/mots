import React from 'react';
import { View, Text } from 'react-native';
import HealthWorkersList from '../container/HealthWorkersList';

import styles from '../styles/listsStyles';

const Incharges = () => (
  <View style={[styles.container, { marginBottom: 70 }]}>
    <Text style={styles.title}>Community Health Workers</Text>
    <HealthWorkersList />
  </View>
);

export default Incharges;

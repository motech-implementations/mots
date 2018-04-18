import React from 'react';
import { View } from 'react-native';

import HealthWorkersList from '../container/HealthWorkersList';
import getContainerStyle from '../utils/styleUtils';

const HealthWorkers = () => (
  <View style={getContainerStyle()}>
    <HealthWorkersList title="Selected CHWs" selected />
  </View>
);

export default HealthWorkers;

import React from 'react';
import { View, PixelRatio } from 'react-native';

import HealthWorkersList from '../container/HealthWorkersList';
import getContainerStyle from '../utils/styleUtils';

const HealthWorkersOverall = () => (
  <View style={getContainerStyle()}>
    <HealthWorkersList
      title={PixelRatio.get() < 2 ? 'CHW List' : 'Community Health Workers'}
      selected={false}
    />
  </View>
);

export default HealthWorkersOverall;

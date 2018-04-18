import React from 'react';
import { View } from 'react-native';
import InchargeList from '../container/InchargeList';
import getContainerStyle from '../utils/styleUtils';

const Incharges = () => (
  <View style={getContainerStyle()}>
    <InchargeList title="Selected Incharges" selected />
  </View>
);

export default Incharges;

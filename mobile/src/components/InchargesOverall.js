import React from 'react';
import { View } from 'react-native';
import InchargeList from '../container/InchargeList';
import getContainerStyle from '../utils/styleUtils';

const Incharges = () => (
  <View style={getContainerStyle()}>
    <InchargeList title="Incharges" selected={false} />
  </View>
);

export default Incharges;

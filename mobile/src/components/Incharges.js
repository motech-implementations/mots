import React from 'react';
import { View, Text } from 'react-native';
import InchargeList from '../container/InchargeList';
import getContainerStyle from '../utils/styleUtils';
import styles from '../styles/listsStyles';

const Incharges = () => (
  <View style={getContainerStyle()}>
    <Text style={styles.title}>Incharges</Text>
    <InchargeList />
  </View>
);

export default Incharges;

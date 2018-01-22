import React from 'react';
import { View, Text } from 'react-native';
import InchargeList from '../container/InchargeList';

const styles = {
  container: {
    marginTop: 60,
    marginBottom: 20,
    backgroundColor: '#FFF',
  },
  title: {
    fontSize: 36,
    marginHorizontal: 40,
    marginVertical: 10,
  },
};

const Incharges = () => (
  <View style={styles.container}>
    <Text style={styles.title}>Incharges</Text>
    <InchargeList />
  </View>
);

export default Incharges;

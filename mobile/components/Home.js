import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});

const Home = () => (
  <View style={styles.container}>
    <Text>Welcome to Mots React-native app.</Text>
  </View>
);

export default Home;

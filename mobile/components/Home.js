import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    backgroundColor: '#FFF',
    padding: 20,
  },
  title: {
    fontSize: 24,
    borderBottomColor: '#9b9b9b',
    borderBottomWidth: 1,
  },
});

const Home = () => (
  <View style={styles.container}>
    <Text style={styles.title}>Home Page</Text>
    <Text>As in many African countries, the Community Health Worker (CHW) is
      the backbone of the healthcare system, and the focus and objective
      of the Mobile Training and Support (MOTS) project will be strengthening
      this network to ensure preparedness for Ebola vaccine campaigns and
      outbreak response. {'\n'} {'\n'}
      The project is setting up a MOTS service that is
      the vehicle for delivery of this training. This service will provide
      mobile training to CHWs via their mobile phones with feature phone as the
      basic requirement. The MOTS service will provide training modules that
      include units and quizzes, and allow monitoring by management.
    </Text>
  </View>
);

export default Home;

import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#FFF',
    flexDirection: 'column',
  },
  menuItem: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    borderBottomColor: '#e7e7e7',
    borderBottomWidth: 1,
    height: 50,
  },
  menuItemText: {
    fontSize: 16,
    color: '#337ab7',
    paddingLeft: 5,
  },
};

const Menu = () => (
  <View style={styles.container}>
    <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
      <Text style={styles.menuItemText}>Home</Text>
    </TouchableOpacity>
    <TouchableOpacity onPress={Actions.incharges} style={styles.menuItem}>
      <Text style={styles.menuItemText}>Incharge List</Text>
    </TouchableOpacity>
  </View>
);

export default Menu;

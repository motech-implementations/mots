import React from 'react';
import { Text, View, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';

const styles = {
  viewStyle: {
    flexDirection: 'row',
    backgroundColor: '#e1e1e1',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 60,
    paddingTop: 15,
  },
  textStyle: {
    fontSize: 20,
    paddingLeft: 10,
    color: '#9b9b9b',
  },
  iconStyle: {
    paddingRight: 10,
  },
};

const Header = () => (
  <View style={styles.viewStyle}>
    <Text style={styles.textStyle}>MOTS</Text>
    <TouchableOpacity onPress={Actions.menu} style={styles.iconStyle}>
      <Text>Menu</Text>
    </TouchableOpacity>
  </View>
);


export default Header;

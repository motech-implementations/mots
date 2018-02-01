import React, { Component } from 'react';
import { Text, View, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';

const styles = {
  viewStyle: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    flexDirection: 'row',
    backgroundColor: '#e1e1e1',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: 40,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    elevation: 2,
  },
  textStyle: {
    fontSize: 20,
    paddingRight: 10,
    color: '#9b9b9b',
  },
  iconStyle: {
    paddingLeft: 10,
  },
};

class Header extends Component {
  static contextTypes = {
    drawer: PropTypes.object,
  };

  state = {};

  render() {
    return (
      <View style={styles.viewStyle}>
        <TouchableOpacity
          onPress={this.context.drawer.open}
          style={styles.iconStyle}
        >
          <Icon
            name="bars"
            size={24}
          />
        </TouchableOpacity>
        <Text style={styles.textStyle}>MOTS</Text>
      </View>
    );
  }
}

export default Header;

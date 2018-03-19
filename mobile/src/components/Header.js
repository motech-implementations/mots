import React, { Component } from 'react';
import { Text, View, TouchableOpacity, Dimensions } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';
import commonStyles from '../styles/commonStyles';

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
  viewStyleHighResolution: {
    height: 60,
  },
  textStyle: {
    fontSize: 20,
    paddingRight: 10,
    color: '#9b9b9b',
  },
  fontSizeStyleHighResolution: {
    fontSize: 40,
  },
  iconStyle: {
    paddingLeft: 10,
  },
};

const { lightThemeText } = commonStyles;
const screen = Dimensions.get('screen');

class Header extends Component {
  static contextTypes = {
    drawer: PropTypes.object,
  };

  state = {
    highResolution: screen.height >= 800 || screen.width >= 800,
  };

  getViewStyle() {
    return [styles.viewStyle, this.state.highResolution && styles.viewStyleHighResolution];
  }

  getTextStyle() {
    return [styles.textStyle, this.state.highResolution && styles.fontSizeStyleHighResolution];
  }

  getIconSize() {
    return this.state.highResolution ? 48 : 24;
  }

  render() {
    return (
      <View style={this.getViewStyle()}>
        <TouchableOpacity
          onPress={this.context.drawer.open}
          style={styles.iconStyle}
        >
          <Icon
            name="bars"
            size={this.getIconSize()}
            style={lightThemeText}
          />
        </TouchableOpacity>
        <Text style={this.getTextStyle()}>Mobile Training and Support</Text>
      </View>
    );
  }
}

export default Header;

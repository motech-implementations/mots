import React, { Component } from 'react';
import { Animated, Easing } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';

class SpinningIcon extends Component {
  componentDidMount() {
    this.spin();
  }
  spinValue = new Animated.Value(0);

  spin = () => {
    this.spinValue.setValue(0);

    Animated.timing(
      this.spinValue,
      {
        toValue: 1,
        duration: 1000,
        easing: Easing.linear,
        useNativeDriver: true,
      },
    ).start(() => this.spin());
  };

  render() {
    const { name, size, color } = this.props;
    const rotate = this.spinValue.interpolate({ inputRange: [0, 1], outputRange: ['0deg', '360deg'] });

    return (
      <Animated.View style={{ transform: [{ rotate }] }}>
        <Icon name={name} size={size} color={color} />
      </Animated.View>
    );
  }
}

export default SpinningIcon;

SpinningIcon.propTypes = {
  name: PropTypes.string.isRequired,
  size: PropTypes.number.isRequired,
  color: PropTypes.string.isRequired,
};

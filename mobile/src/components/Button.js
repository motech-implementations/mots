import React from 'react';
import { Text, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';
import SpinningIcon from './SpinningIcon';

const defaultStyles = {
  actionButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 3,
    backgroundColor: '#337ab7',
    paddingHorizontal: 10,
    paddingVertical: 5,
  },
  buttonLabel: {
    marginLeft: 5,
    color: '#FFF',
  },
};

const Button = ({
  onPress, children, iconName, iconColor, buttonColor, style, disabled, spinning,
}) => {
  const { actionButton, buttonLabel } = defaultStyles;
  const buttonStyles = {
    backgroundColor: (disabled) ? '#c3c3c3' : buttonColor,
    ...style,
  };
  return (
    <TouchableOpacity
      onPress={onPress}
      style={[actionButton, buttonStyles]}
      disabled={disabled}
    >
      {spinning ? (
        <SpinningIcon name={iconName} size={16} color={iconColor} />
      ) : (
        <Icon name={iconName} size={16} color={iconColor} />
      )}
      <Text style={buttonLabel}>
        {children}
      </Text>
    </TouchableOpacity>
  );
};

export default Button;

Button.propTypes = {
  iconName: PropTypes.string.isRequired,
  iconColor: PropTypes.string.isRequired,
  buttonColor: PropTypes.string.isRequired,
  children: PropTypes.string,
  onPress: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  spinning: PropTypes.bool,
  style: PropTypes.shape({}),
};

Button.defaultProps = {
  children: '',
  disabled: false,
  style: {},
  spinning: false,
};

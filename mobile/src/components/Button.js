import React from 'react';
import { Text, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';

const styles = {
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
  onPress, children, iconName, iconColor, buttonColor, marginLeft,
}) => {
  const { actionButton, buttonLabel } = styles;

  return (
    <TouchableOpacity
      onPress={onPress}
      style={[actionButton, { backgroundColor: buttonColor, marginLeft }]}
    >
      <Icon name={iconName} size={16} color={iconColor} />
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
  marginLeft: PropTypes.number,
};

Button.defaultProps = {
  children: '',
  marginLeft: 0,
};

import React from 'react';
import { TextInput, View } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';

import styles from '../styles/inputsStyles';

const InputWithIcon = ({
  iconName, iconColor, iconSize, value, onChangeText, placeholder, secureTextEntry,
}) => {
  const { iconInputStyle, iconStyle, containerStyle } = styles;

  return (
    <View style={containerStyle}>
      <View
        style={iconStyle}
      >
        <Icon
          name={iconName}
          color={iconColor}
          size={iconSize}
        />
      </View>

      <TextInput
        secureTextEntry={secureTextEntry}
        placeholder={placeholder}
        autoCorrect={false}
        style={iconInputStyle}
        value={value}
        onChangeText={onChangeText}
      />
    </View>
  );
};

export default InputWithIcon;

InputWithIcon.propTypes = {
  iconName: PropTypes.string,
  iconColor: PropTypes.string,
  iconSize: PropTypes.number,
  value: PropTypes.string,
  onChangeText: PropTypes.func.isRequired,
  placeholder: PropTypes.string,
  secureTextEntry: PropTypes.bool,
};

InputWithIcon.defaultProps = {
  iconName: 'user',
  iconColor: '#000',
  iconSize: 16,
  value: '',
  placeholder: '',
  secureTextEntry: false,
};

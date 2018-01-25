import React from 'react';
import { TextInput, View, Text } from 'react-native';
import PropTypes from 'prop-types';
import styles from '../styles/inputsStyles';

const InputWithLabel = ({
  label, value, onChangeText, placeholder, secureTextEntry,
}) => {
  const { labelInputStyle, labelStyle, containerStyle } = styles;

  return (
    <View style={containerStyle}>
      <Text style={labelStyle}>{label}</Text>
      <TextInput
        secureTextEntry={secureTextEntry}
        placeholder={placeholder}
        autoCorrect={false}
        style={labelInputStyle}
        value={value}
        onChangeText={onChangeText}
      />
    </View>
  );
};

export default InputWithLabel;

InputWithLabel.propTypes = {
  label: PropTypes.string.isRequired,
  value: PropTypes.string,
  onChangeText: PropTypes.func.isRequired,
  placeholder: PropTypes.string,
  secureTextEntry: PropTypes.bool,
};

InputWithLabel.defaultProps = {
  value: '',
  placeholder: '',
  secureTextEntry: false,
};

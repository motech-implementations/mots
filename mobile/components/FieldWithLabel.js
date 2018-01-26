import React from 'react';
import { View, Text } from 'react-native';
import PropTypes from 'prop-types';
import styles from '../styles/inputsStyles';

const FieldWithLabel = ({ label, children, isSelectField }) => {
  const {
    labelFieldStyle,
    labelStyle,
    containerStyle,
    labelFieldSelectStyle,
  } = styles;

  return (
    <View style={containerStyle}>
      <Text style={labelStyle}>{label}</Text>
      <View style={[labelFieldStyle, isSelectField ? labelFieldSelectStyle : '']}>
        {children}
      </View>
    </View>
  );
};

export default FieldWithLabel;

FieldWithLabel.propTypes = {
  label: PropTypes.string.isRequired,
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node,
  ]),
  isSelectField: PropTypes.bool,
};

FieldWithLabel.defaultProps = {
  children: <View />,
  isSelectField: false,
};

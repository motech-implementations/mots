import React from 'react';
import { View, Text } from 'react-native';
import PropTypes from 'prop-types';
import styles from '../styles/inputsStyles';

const FieldWithLabel = ({ label, children, nonBorderField }) => {
  const {
    labelFieldStyle,
    labelStyle,
    containerStyle,
    nonBorderFieldStyle,
  } = styles;

  return (
    <View style={containerStyle}>
      <Text style={labelStyle}>{label}</Text>
      <View style={[labelFieldStyle, nonBorderField ? nonBorderFieldStyle : '']}>
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
  nonBorderField: PropTypes.bool,
};

FieldWithLabel.defaultProps = {
  children: <View />,
  nonBorderField: false,
};

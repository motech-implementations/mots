import React from 'react';
import { View, Text, PixelRatio } from 'react-native';
import PropTypes from 'prop-types';
import styles from '../styles/inputsStyles';
import commonStyles from '../styles/commonStyles';

const FieldWithLabel = ({ label, children, nonBorderField }) => {
  const {
    labelFieldStyle,
    labelStyle,
    labelStyleSmall,
    containerStyle,
    nonBorderFieldStyle,
  } = styles;
  const { lightThemeText } = commonStyles;

  return (
    <View style={containerStyle}>
      <Text style={[labelStyle, lightThemeText, PixelRatio.get() < 2 && labelStyleSmall]}>
        {label}
      </Text>
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

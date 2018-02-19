import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { View, TextInput, Text } from 'react-native';
import { Option } from 'react-native-chooser';
import FieldWithLabel from '../components/FieldWithLabel';

function renderSelectOptions(options) {
  const { values, valueKey, displayNameKey } = options;
  const emptyValue = '';

  return [
    _.map(values, (value, index) => {
      const rawValue = valueKey ? value[valueKey] : value;
      const optionValue = typeof rawValue === 'string' ? rawValue : JSON.stringify(rawValue);
      const displayValue = displayNameKey ? value[displayNameKey] : value;

      return (
        <Option key={index} value={optionValue}>
          { displayValue || '' }
        </Option>);
    }),
    <Option key="undefined" value={null}>{emptyValue}</Option>,
  ];
}

export default class FormField extends Component {
  renderFieldInput = ({
    fieldConfig, selectOptions, dynamicAttr, input, meta: { touched, error },
  }) => {
    const {
      label, type, getAttributes, nonBorderField, required,
    } = fieldConfig;
    const labelWithAsterisk = required && `${label}*`;
    const FieldType = type || TextInput;
    const attr = getAttributes ? getAttributes(input, this.props) : { ...input };
    const attributes = {
      value: input.value,
      disabled: selectOptions && (!selectOptions.values || !selectOptions.values.length),
      label,
      ...attr,
      ...dynamicAttr,
    };
    const style = attributes.editable === false
      ? { ...attributes.style, backgroundColor: '#dddddd' }
      : { ...attributes.style };

    return (
      !attributes.hidden &&
      <View>
        <FieldWithLabel
          label={labelWithAsterisk || label}
          nonBorderField={nonBorderField}
        >
          <FieldType
            {...attributes}
            onChangeText={text => input.onChange(text)}
            value={input.value}
            style={style}
          >
            {
              selectOptions && renderSelectOptions(selectOptions)
            }
          </FieldType>
        </FieldWithLabel>
        <View>
          <Text style={{ color: 'red' }}>{ touched ? error : '' }</Text>
        </View>
      </View>
    );
  };

  render() {
    const { fieldName, fieldConfig } = this.props;

    return (
      <Field
        name={fieldName}
        component={this.renderFieldInput}
        fieldConfig={fieldConfig}
        selectOptions={
          fieldConfig.getSelectOptions
            ? fieldConfig.getSelectOptions(this.props)
            : null
        }
        dynamicAttr={
          fieldConfig.getDynamicAttributes ? fieldConfig.getDynamicAttributes(this.props) : {}
        }
      />
    );
  }
}

FormField.propTypes = {
  fieldName: PropTypes.string.isRequired,
  fieldConfig: PropTypes.shape({
    label: PropTypes.string.isRequired,
    type: PropTypes.oneOfType([
      PropTypes.string,
      PropTypes.func,
    ]),
    required: PropTypes.bool,
    getAttributes: PropTypes.func,
    getSelectOptions: PropTypes.func,
    getDynamicAttributes: PropTypes.func,
  }).isRequired,
};

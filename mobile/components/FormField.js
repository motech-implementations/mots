import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { View, TextInput, Text } from 'react-native';
import { Option } from 'react-native-chooser';
import FieldWithLabel from '../components/FieldWithLabel';

function renderSelectOptions(options) {
  const { values, valueKey, displayNameKey } = options;

  return [
    _.map(values, (value, index) => {
      const rawValue = valueKey ? value[valueKey] : value;
      const optionValue = typeof rawValue === 'string' ? rawValue : JSON.stringify(rawValue);
      const displayValue = displayNameKey ? value[displayNameKey] : value;

      return (
        <Option key={index} value={optionValue} disabled={value.disabled}>
          { displayValue || '' }
        </Option>);
    }),
  ];
}

export default class FormField extends Component {
  renderFieldInput = ({
    fieldConfig, selectOptions, dynamicAttr, input, meta: { touched, error },
  }) => {
    const { label, type, getAttributes } = fieldConfig;
    const FieldType = type || TextInput;
    const attr = getAttributes ? getAttributes(input) : { ...input };
    const attributes = {
      value: input.value,
      disabled: selectOptions && (!selectOptions.values || !selectOptions.values.length),
      label,
      ...attr,
      ...dynamicAttr,
    };

    return (
      <View>
        <View className="row">
          <View className="col-md-4">
            <FieldWithLabel
              label={label}
              nonBorderField={FieldType !== TextInput}
            >
              <FieldType
                {...attributes}
                onChangeText={text => input.onChange(text)}
                value={input.value}
              >
                {
                  selectOptions && renderSelectOptions(selectOptions)
                }
              </FieldType>
            </FieldWithLabel>
            <View>
              <Text>{ touched ? error : '' }</Text>
            </View>
          </View>
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

import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { View, TextInput, Text } from 'react-native';
import { Select, Option } from 'react-native-chooser';
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
    fieldConfig, selectOptions, dynamicAttr, input: { onChange, ...restInput }, meta: { touched, error },
  }) => {
    const { label, type, getAttributes } = fieldConfig;
    console.log(restInput);
    const FieldType = type || TextInput;
    const attr = getAttributes ? getAttributes(restInput) : { type: 'text', className: 'form-control', ...restInput };
    const attributes = {
      // id: restInput.name,
      value: restInput.value,
      disabled: selectOptions && (!selectOptions.values || !selectOptions.values.length),
      label,
      // ...attr,
      // ...dynamicAttr,
    };

    // const className = `form-group ${fieldConfig.required ? 'required' : ''} ${attributes.hidden ? 'hidden' : ''} ${touched && error ? 'has-error' : ''}`;

    return (
      // <View className={`padding-left-md padding-right-md ${className}`}>
      <View>
        <View className="row">
          {/* <label htmlFor={attributes.id} className="col-md-2 control-label">{ label }</label> */}
          <View className="col-md-4">
            <FieldWithLabel
              label={label}
              isSelectField={FieldType === Select}
            >
              <FieldType {...attributes} onChangeText={(text) => { console.log(text); onChange(text) }} value={restInput.value}>
                {
                  selectOptions && renderSelectOptions(selectOptions)
                }
              </FieldType>
            </FieldWithLabel>
          </View>
        </View>
        {/* <View className="row">
          <View className="col-md-2" />
          <View className="help-block col-md-4" style={{ float: 'left' }}>
            { touched ? error : '' }
          </View>
        </View> */}
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

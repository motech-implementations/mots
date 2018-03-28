import _ from 'lodash';
import { connect } from 'react-redux';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';

import { resetLogoutCounter } from '../actions/index';

function renderSelectOptions(options) {
  const { values, valueKey, displayNameKey } = options;

  return [
    <option key="empty" value={null} />,
    _.map(values, (value, index) => {
      const rawValue = valueKey ? value[valueKey] : value;
      const optionValue = typeof rawValue === 'string' ? rawValue : JSON.stringify(rawValue);
      const displayValue = displayNameKey ? value[displayNameKey] : value;

      return (
        <option key={index} value={optionValue} disabled={value.disabled}>
          { displayValue }
        </option>);
    }),
  ];
}

class FormField extends Component {
  renderFieldInput = ({
    fieldConfig, selectOptions, dynamicAttr, dynamicProps, input, meta: { touched, error },
  }) => {
    const { label, type, getAttributes } = fieldConfig;

    const FieldType = type || 'input';
    const attr = getAttributes ? getAttributes(input, dynamicProps) : { type: 'text', className: 'form-control', ...input };
    const attributes = {
      id: input.name,
      disabled: selectOptions && (!selectOptions.values || !selectOptions.values.length),
      ...attr,
      ...dynamicAttr,
      onFocus: () => {
        this.props.resetLogoutCounter();
      },
    };

    const className = `form-group ${fieldConfig.required || attributes.required ? 'required' : ''} ${attributes.hidden ? 'hidden' : ''} ${touched && error ? 'has-error' : ''}`;
    return (
      <div className={`padding-left-md padding-right-md ${className}`}>
        <div className="row">
          <label htmlFor={attributes.id} className="col-md-2 control-label">{ attributes.label || label }</label>
          <div className="col-md-4">
            <FieldType {...attributes}>
              {
                selectOptions && renderSelectOptions(selectOptions)
              }
            </FieldType>
          </div>
        </div>
        <div className="row">
          <div className="col-md-2" />
          <div className="help-block col-md-4" style={{ float: 'left' }}>
            { touched ? error : '' }
          </div>
        </div>
      </div>
    );
  };

  render() {
    const { fieldName, fieldConfig, dynamicProps } = this.props;

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
        dynamicProps={dynamicProps}
      />
    );
  }
}

export default connect(null, { resetLogoutCounter })(FormField);

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
  dynamicProps: PropTypes.shape({}),
  resetLogoutCounter: PropTypes.func.isRequired,
};

FormField.defaultProps = {
  dynamicProps: {},
};

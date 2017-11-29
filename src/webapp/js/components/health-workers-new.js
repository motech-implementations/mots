import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';
import Alert from 'react-s-alert';

import 'react-datetime/css/react-datetime.css';

import { createHealthWorker } from '../actions';

const FIELDS = {
  chwId: {
    label: 'CHW Id',
    required: true,
  },
  firstName: {
    label: 'First Name',
  },
  secondName: {
    label: 'Surname',
  },
  otherName: {
    label: 'Other Name',
  },
  dateOfBirth: {
    label: 'Date of Birth',
    type: DateTime,
    getAttributes: (input) => {
      const dateFormat = 'YYYY-MM-DD';

      return {
        dateFormat,
        timeFormat: false,
        closeOnSelect: true,
        value: input.value,
        onChange: (param) => {
          const formatted = !param || typeof param === 'string' ? param : param.format(dateFormat);
          input.onChange(formatted);
        },
      };
    },
  },
  gender: {
    type: 'select',
    label: 'Gender',
    getSelectOptions: () => ({
      values: ['Male', 'Female'],
    }),
  },
  literacy: {
    type: 'select',
    label: 'Literacy',
    getSelectOptions: () => ({
      values: ['Can read and write', 'Cannot read and write', 'Can only read'],
    }),
  },
  educationLevel: {
    type: 'select',
    label: 'Educational Level',
    getSelectOptions: () => ({
      values: ['Primary', 'Secondary', 'Higher', 'None'],
    }),
  },
  phoneNumber: {
    label: 'Phone Number',
    required: true,
  },
  hasPeerSupervisor: {
    getAttributes: input => ({ type: 'checkbox', ...input }),
    label: 'Peer Supervisor',
  },
  supervisor: {
    label: 'Supervisor',
  },
  preferredLanguage: {
    type: 'select',
    label: 'Preferred Language',
    getSelectOptions: () => ({
      values: ['English', 'Krio', 'Limba', 'Susu', 'Temne'],
    }),
  },
};

class HealthWorkersNew extends Component {
  static renderSelectOptions(options) {
    const { values, valueKey, displayNameKey } = options;

    return [
      <option key="empty" value={null} />,
      _.map(values, (value, index) => (
        <option key={index} value={valueKey ? value[valueKey] : value}>
          { displayNameKey ? value[displayNameKey] : value }
        </option>)),
    ];
  }

  constructor(props) {
    super(props);

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.renderField = this.renderField.bind(this);
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  onSubmit(values) {
    this.props.createHealthWorker(values, () => {
      Alert.success('CHW has been added');
      this.props.history.push('/chw');
    });
  }

  renderInput = ({ fieldConfig, input, meta: { touched, error } }) => {
    const {
      label, type, getAttributes, getSelectOptions,
    } = fieldConfig;

    const className = `form-group ${fieldConfig.required && 'required'} ${touched && error && 'has-error'}`;
    const FieldType = type || 'input';
    const attributes = getAttributes ? getAttributes(input) : { type: 'text', className: 'form-control', ...input };

    return (
      <div className={className}>
        <div className="row">
          <label htmlFor={input.name} className="col-md-2 control-label">{ label }</label>
          <div className="col-md-4">
            <FieldType {...attributes} id={input.name} >
              { getSelectOptions
              && HealthWorkersNew.renderSelectOptions(getSelectOptions(this.props)) }
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

  renderField(fieldConfig, fieldName) {
    return (
      <Field
        fieldConfig={fieldConfig}
        key={fieldName}
        name={fieldName}
        component={this.renderInput}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <div>
        <h1 className="page-header">Add Community Health Worker</h1>
        <form className="form-horizontal" onSubmit={handleSubmit(this.onSubmit)}>
          { _.map(FIELDS, this.renderField) }
          <div className="col-md-2" />
          <button type="submit" className="btn btn-primary">Submit</button>
          <button className="btn btn-danger" onClick={this.onSubmitCancel}>Cancel</button>
        </form>
      </div>
    );
  }
}

function validate(values) {
  const errors = {};

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  return errors;
}

export default reduxForm({
  validate,
  form: 'HealthWorkersNewForm',
})(connect(null, { createHealthWorker })(HealthWorkersNew));

HealthWorkersNew.propTypes = {
  createHealthWorker: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  handleSubmit: PropTypes.func.isRequired,
};

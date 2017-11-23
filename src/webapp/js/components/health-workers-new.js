import _ from 'lodash';
import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import DateTime from 'react-datetime'

import 'react-datetime/css/react-datetime.css';

import { createHealthWorker } from '../actions';

const FIELDS = {
  chwId: {
    label: 'CHW Id',
    required: true
  },
  firstName: {
    label: 'First Name'
  },
  secondName: {
    label: 'Surname'
  },
  otherName: {
    label: 'Other Name'
  },
  dateOfBirth: {
    label: 'Date of Birth',
    type: DateTime,
    getAttributes: input => {
      const dateFormat = 'YYYY-MM-DD';

      return {
        dateFormat: dateFormat,
        timeFormat: false,
        closeOnSelect: true,
        value: input.value,
        onChange: param => {
          const formatted = !param || typeof param === 'string' ? param : param.format(dateFormat);
          input.onChange(formatted);
        }
      }
    }
  },
  gender: {
    type: 'select',
    label: 'Gender',
    getSelectOptions: () => {
      return {
        values: ['Male', 'Female']
      };
    }
  },
  literacy: {
    type: 'select',
    label: 'Literacy',
    getSelectOptions: () => {
      return {
        values: ['Can read and write', 'Cannot read and write', 'Can only read']
      };
    }
  },
  educationLevel: {
    type: 'select',
    label: 'Educational Level',
    getSelectOptions: () => {
      return {
        values: ['Primary', 'Secondary', 'Higher', 'None']
      };
    }
  },
  phoneNumber: {
    label: 'Phone Number',
    required: true
  },
  hasPeerSupervisor: {
    getAttributes: input => {
      return { type: 'checkbox', ...input }
    },
    label: 'Peer Supervisor'
  },
  supervisor: {
    label: 'Supervisor'
  },
  preferredLanguage: {
    type: 'select',
    label: 'Preferred Language',
    getSelectOptions: () => {
      return {
        values: ['English', 'Krio', 'Limba', 'Susu', 'Temne']
      };
    }
  }
};

class HealthWorkersNew extends Component {

  renderSelectOptions(options) {
    const { values, valueKey, displayNameKey } = options;

    return [
        <option key="empty" value={ null }></option>,
        _.map(values, (value, index) => <option key={ index } value={ valueKey ? value[valueKey] : value }>
          { displayNameKey ? value[displayNameKey] : value }</option>)
    ];
  }

  renderInput = ({ fieldConfig, input, meta: { touched, error } }) => {
    const { label, type, getAttributes, getSelectOptions } = fieldConfig;

    const className = `form-group ${ fieldConfig.required && 'required' } ${ touched && error && 'has-error' }`;
    const FieldType = type ? type : 'input';
    const attributes = getAttributes ? getAttributes(input) : { type: 'text', className: 'form-control', ...input };

    return (
        <div className={ className }>
          <div className="row">
            <label className="col-md-2 control-label">{ label }</label>
            <div className="col-md-4">
              <FieldType { ...attributes }  >
                { getSelectOptions && this.renderSelectOptions(getSelectOptions(this.props)) }
              </FieldType>
            </div>
          </div>
          <div className="row">
            <div className="col-md-2" />
            <div className="help-block col-md-4" style={{float: 'left'}}>
              { touched ? error : '' }
            </div>
          </div>
        </div>
    );
  };

  renderField(fieldConfig, fieldName) {
    return (
        <Field
            fieldConfig={ fieldConfig }
            key={ fieldName }
            name={ fieldName }
            component={ this.renderInput }
        />
    );
  }

  onSubmit(values) {
    this.props.createHealthWorker(values, () => {
      this.props.history.push('/chw');
    });
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  render() {
    const { handleSubmit } = this.props;

    return (
        <div>
          <div>
            <h1 className="page-header">Add Community Health Worker</h1>
            <form className="form-horizontal" onSubmit={ handleSubmit(this.onSubmit.bind(this)) }>
              { _.map(FIELDS, this.renderField.bind(this)) }
              <div className="col-md-2" />
              <button type="submit" className="btn btn-primary">Submit</button>
              <button className="btn btn-danger" onClick={ this.onSubmitCancel.bind(this) }>Cancel</button>
            </form>
          </div>
        </div>
    );
  }
}

function validate(values) {
  const errors = {};

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = `This field is required`;
    }
  });

  return errors;
}

export default reduxForm({
  validate,
  form: 'HealthWorkersNewForm'
})(
    connect(null, { createHealthWorker })(HealthWorkersNew)
);

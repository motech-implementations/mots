import _ from 'lodash';
import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { updateAutomatedReports } from '../actions/index';
import FormField from './form-field';
import DatePicker from '../utils/date-picker';

export const AUTOMATED_REPORT_SETTINGS_FORM_NAME = 'AutomatedSettingsForm';

const FIELDS = {
  jobName: {
    label: 'Name',
    getAttributes: input => ({
      disabled: true,
      type: 'text',
      className: 'form-control',
      ...input,
    }),
  },
  startDate: {
    label: 'Start date',
    type: DatePicker,
    getAttributes: input => ({
      ...input,
      showTimeSelect: true,
      className: 'form-control',
      value: input.value,
    }),
  },
  intervalInSeconds: {
    minValue: 60,
    label: 'Interval in seconds',
    required: true,
  },
  emails: {
    type: props => <textarea {...props} />,
    label: 'Emails',
    getAttributes: input => ({
      type: 'text-area',
      className: 'form-control',
      ...input,
    }),
  },
  enabled: {
    label: 'Enabled',
    getAttributes: input => ({
      ...input,
      type: 'checkbox',
      className: 'form-control',
      checked: input.value,
    }),
  },
};

class AutomatedSettingsEdit extends Component {
  onSubmit = (values) => {
    const date = new Date(Date.parse(values.startDate));
    this.props.updateAutomatedReports({ ...values, startDate: date.getTime() },
      () => this.redirectToList());
  };

  redirectToList = () => {
    this.props.history.push({
      pathname: '/automatedReports',
    });
  };

  renderField = (fieldConfig, fieldName) => (
    <FormField
      key={fieldName}
      fieldName={fieldName}
      fieldConfig={fieldConfig}
    />
  );

  render() {
    const { handleSubmit } = this.props;
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Automated Report Setting</h1>
        <form onSubmit={handleSubmit(this.onSubmit)}>
          { _.map(FIELDS, this.renderField) }
          <div className="row">
            <div className="col-md-2" />
            <div className="col-md-10">
              <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
              <button
                type="button"
                className="btn btn-danger margin-left-sm margin-bottom-md"
                onClick={this.redirectToList}
              >
                Cancel
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}

function validate(values) {
  const errors = {};

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.minValue && values[fieldName] < 60) {
      errors[fieldName] = 'It can not be less than 60 seconds';
    }
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  return errors;
}

export default reduxForm({
  validate,
  form: AUTOMATED_REPORT_SETTINGS_FORM_NAME,
})(connect(null, { updateAutomatedReports })(AutomatedSettingsEdit));

AutomatedSettingsEdit.propTypes = {
  updateAutomatedReports: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};

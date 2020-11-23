import _ from 'lodash';
import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import FormField from './form-field';
import DatePicker from '../utils/date-picker';
import { PERIOD_OPTIONS } from '../constants/period';
import { updateAutomatedReports } from '../actions/index';

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
  period: {
    required: true,
    type: 'select',
    label: 'Period',
    getSelectOptions: () => ({
      values: PERIOD_OPTIONS,
    }),
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
  messageSubject: {
    label: 'Message Subject',
    getAttributes: input => ({
      type: 'text',
      className: 'form-control',
      ...input,
    }),
  },
  messageBody: {
    type: props => <textarea {...props} />,
    label: 'Message Body',
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
    const { downloadPdf } = this.props.location.state;
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Edit Automated Report Setting</h1>
        <button
          onClick={downloadPdf}
          type="button"
          className="btn btn-success margin-left-sm margin-bottom-lg"
        >
          Download PDF
        </button>
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
  location: PropTypes.shape({
    state: PropTypes.shape({
      downloadPdf: PropTypes.func.isRequired,
    }),
  }).isRequired,
};

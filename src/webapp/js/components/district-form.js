import _ from 'lodash';
import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';

import FormField from './form-field';

export const DISTRICT_FORM_NAME = 'DistrictForm';

const FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
};

class DistrictForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  // eslint-disable-next-line class-methods-use-this
  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <form onSubmit={handleSubmit(this.props.onSubmit)}>
        {_.map(FIELDS, this.renderField)}
        <div className="row">
          <div className="col-md-2" />
          <div className="col-md-10">
            <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
            <button type="button" className="btn btn-danger margin-left-sm margin-bottom-md" onClick={this.props.onSubmitCancel}>Cancel</button>
          </div>
        </div>
      </form>
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
  form: DISTRICT_FORM_NAME,
})(DistrictForm);

DistrictForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

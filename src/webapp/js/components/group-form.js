import _ from 'lodash';
import React from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';

import FormField from './form-field';

export const GROUP_FORM_NAME = 'GroupForm';

const FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
};

const GroupForm = ({ handleSubmit, onSubmit, onSubmitCancel }) => {
  const renderField = (fieldConfig, fieldName) => (
    <FormField
      key={fieldName}
      fieldName={fieldName}
      fieldConfig={fieldConfig}
    />
  );

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      { _.map(FIELDS, renderField) }
      <div className="row">
        <div className="col-md-2" />
        <div className="col-md-10">
          <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
          <button type="button" className="btn btn-danger margin-left-sm margin-bottom-md" onClick={onSubmitCancel}>Cancel</button>
        </div>
      </div>
    </form>
  );
};

function validate(values) {
  const errors = {};

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  return errors;
}

export default reduxForm({ validate, form: GROUP_FORM_NAME })(GroupForm);

GroupForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

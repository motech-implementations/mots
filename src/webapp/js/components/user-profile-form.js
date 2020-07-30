import _ from 'lodash';
import React, { Component } from 'react';
import { formValueSelector, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';

export const USER_PROFILE_FORM_NAME = 'UserProfileForm';

const FIELDS = {
  username: {
    label: 'Username',
    getAttributes: input => ({
      disabled: true,
      type: 'text',
      className: 'form-control',
      autoComplete: 'username',
      ...input,
    }),
  },
  name: {
    label: 'Name',
  },
  email: {
    label: 'Email',
  },
  newPassword: {
    label: 'New Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
      autoComplete: 'new-password',
    }),
  },
  confirmNewPassword: {
    label: 'Confirm New Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
      autoComplete: 'new-password',
    }),
    getDynamicAttributes: ({ newPassword }) => ({
      hidden: !newPassword,
      required: newPassword,
    }),
  },
  password: {
    label: 'Current Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
      autoComplete: 'current-password',
    }),
    getDynamicAttributes: ({ confirmNewPassword }) => ({
      hidden: !confirmNewPassword,
      required: confirmNewPassword,
    }),
  },
};

class UserProfileForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        password={this.props.password}
        newPassword={this.props.newPassword}
        confirmNewPassword={this.props.confirmNewPassword}
        isPasswordRequired={this.props.isPasswordRequired}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <form onSubmit={handleSubmit(this.props.onSubmit)}>
        { _.map(FIELDS, this.renderField) }
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
  const emailPattern = /\S+@\S+\.\S+/;

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });
  if (values.newPassword && values.confirmNewPassword
    && values.newPassword !== values.confirmNewPassword) {
    errors.confirmNewPassword = "Passwords don't match";
  }
  if (values.newPassword && !values.confirmNewPassword) {
    errors.confirmNewPassword = 'Can not be empty';
  }
  if (values.confirmNewPassword && !values.password) {
    errors.password = 'Can not be empty';
  }
  if (values.email && !emailPattern.test(values.email)) {
    errors.email = 'Email address has incorrect format';
  }

  return errors;
}

const selector = formValueSelector(USER_PROFILE_FORM_NAME);

function mapStateToProps(state) {
  return {
    newPassword: selector(state, 'newPassword'),
    confirmNewPassword: selector(state, 'confirmNewPassword'),
    password: selector(state, 'password'),
  };
}

export default reduxForm({
  validate,
  form: USER_PROFILE_FORM_NAME,
})(connect(mapStateToProps)(UserProfileForm));

UserProfileForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  newPassword: PropTypes.string,
  password: PropTypes.string,
  confirmNewPassword: PropTypes.string,
  isPasswordRequired: PropTypes.bool,
};

UserProfileForm.defaultProps = {
  password: null,
  newPassword: null,
  confirmNewPassword: null,
  isPasswordRequired: true,
};

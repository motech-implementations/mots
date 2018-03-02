import _ from 'lodash';
import React, { Component } from 'react';
import { formValueSelector, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';

export const PROFILE_FORM_NAME = 'ProfileForm';

const FIELDS = {
  name: {
    label: 'Name',
  },
  email: {
    label: 'Email',
  },
  password: {
    label: 'Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
    }),
    getDynamicAttributes: ({ isPasswordRequired }) => ({
      required: isPasswordRequired,
    }),
  },
  passwordConfirm: {
    label: 'Confirm Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
    }),
    getDynamicAttributes: ({ password, isPasswordRequired }) => ({
      hidden: !password,
      required: isPasswordRequired,
    }),
  },
};

class ProfileForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        password={this.props.password}
        isPasswordRequired={this.props.isPasswordRequired}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
        { _.map(FIELDS, this.renderField) }
        <div className="col-md-2" />
        <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
        <button className="btn btn-danger margin-left-sm margin-bottom-md" onClick={this.props.onSubmitCancel}>Cancel</button>
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
  if (values.password && values.passwordConfirm && values.passwordConfirm !== values.password) {
    errors.passwordConfirm = "Passwords don't match";
  }
  if (values.password && !values.passwordConfirm) {
    errors.passwordConfirm = 'Can not be empty';
  }
  if (values.email && !emailPattern.test(values.email)) {
    errors.email = 'Email address has incorrect format';
  }

  return errors;
}

const selector = formValueSelector(PROFILE_FORM_NAME);


function mapStateToProps(state) {
  return {
    password: selector(state, 'password'),
  };
}

export default reduxForm({
  validate,
  form: PROFILE_FORM_NAME,
})(connect(mapStateToProps)(ProfileForm));

ProfileForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  password: PropTypes.string,
  isPasswordRequired: PropTypes.bool,
};

ProfileForm.defaultProps = {
  password: null,
  isPasswordRequired: true,
};

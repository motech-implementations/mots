import _ from 'lodash';
import React, { Component } from 'react';
import { formValueSelector, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';
import { fetchToken } from '../actions/index';

export const FORM_NAME = 'RegistrationForm';

function getName(registrationToken) {
  let name = '';
  const { incharge } = registrationToken;
  if (incharge) {
    name = incharge.firstName;
    if (incharge.otherName) {
      name += ` ${incharge.otherName}`;
    }
    if (incharge.secondName) {
      name += ` ${incharge.secondName}`;
    }
  }
  return name;
}

const FIELDS = {
  email: {
    label: 'Email',
    getDynamicAttributes: ({ registrationToken }) => ({
      value: (registrationToken) ? registrationToken.email : '',
      disabled: true,
    }),
  },
  username: {
    label: 'Username',
    required: true,
    getAttributes: input => ({
      ...input,
      className: 'form-control',
      autoComplete: 'username',
    }),
  },
  name: {
    label: 'Name',
    getDynamicAttributes: ({ registrationToken }) => ({
      value: (registrationToken) ? getName(registrationToken) : '',
    }),
  },
  password: {
    label: 'Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
      autoComplete: 'new-password',
    }),
  },
  passwordConfirm: {
    label: 'Confirm Password',
    getAttributes: input => ({
      ...input,
      type: 'password',
      className: 'form-control',
      autoComplete: 'new-password',
    }),
    getDynamicAttributes: ({ password }) => ({
      hidden: !password,
    }),
  },
};

class RegistrationForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.props.fetchToken(this.props.token);
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        password={this.props.password}
        registrationToken={this.props.registrationToken}
        largeInput
      />
    );
  }

  render() {
    const { handleSubmit, registrationToken } = this.props;
    console.log(registrationToken);

    if (this.props.error) {
      return (
        <div>
          <div id="login-alert" className="alert alert-danger col-sm-12">
            <strong>This registration link is expired or have already been used.</strong>
          </div>
          <button className="btn btn-primary margin-bottom-md" onClick={this.props.onSubmitCancel}>Sign in</button>
        </div>
      );
    } else if (this.props.registrationToken) {
      return (
        <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
          { _.map(FIELDS, this.renderField) }
          <div className="col-md-2" />
          <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
          <button className="btn btn-danger margin-left-sm margin-bottom-md" onClick={this.props.onSubmitCancel}>Cancel</button>
        </form>
      );
    }
    return null;
  }
}

function validate(values) {
  const errors = {};

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

  return errors;
}

const selector = formValueSelector(FORM_NAME);

function mapStateToProps(state) {
  return {
    registrationToken: state.registrationReducer.registrationToken,
    error: state.registrationReducer.error,
    password: selector(state, 'password'),
  };
}

export default reduxForm({
  validate,
  form: FORM_NAME,
})(connect(mapStateToProps, { fetchToken })(RegistrationForm));

RegistrationForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchToken: PropTypes.func.isRequired,
  token: PropTypes.string.isRequired,
  registrationToken: PropTypes.shape({}),
  error: PropTypes.bool,
  password: PropTypes.string,
};

RegistrationForm.defaultProps = {
  registrationToken: null,
  error: false,
  password: null,
};

import _ from 'lodash';
import React, { Component } from 'react';
import { formValueSelector, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';
import { fetchRoles } from '../actions/index';

export const USER_FORM_NAME = 'UserForm';

const FIELDS = {
  username: {
    label: 'Username',
    required: true,
    getAttributes: input => ({
      ...input,
      className: 'form-control',
      autoComplete: 'username',
    }),
  },
  email: {
    label: 'Email',
  },
  name: {
    label: 'Name',
  },
  roleId: {
    label: 'Role',
    required: true,
    type: 'select',
    getSelectOptions: ({ roles }) => ({
      values: roles,
      displayNameKey: 'name',
      valueKey: 'id',
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
      autoComplete: 'new-password',
    }),
    getDynamicAttributes: ({ password, isPasswordRequired }) => ({
      hidden: !password,
      required: isPasswordRequired,
    }),
  },
};

class UserForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentDidMount() {
    this.props.fetchRoles();
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        roles={this.props.roles}
        password={this.props.password}
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

const selector = formValueSelector(USER_FORM_NAME);

function mapStateToProps(state) {
  return {
    roles: state.tablesReducer.roles,
    password: selector(state, 'password'),
  };
}

export default reduxForm({
  validate,
  form: USER_FORM_NAME,
})(connect(mapStateToProps, { fetchRoles })(UserForm));

UserForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchRoles: PropTypes.func.isRequired,
  roles: PropTypes.arrayOf(PropTypes.shape({})),
  password: PropTypes.string,
  isPasswordRequired: PropTypes.bool,
};

UserForm.defaultProps = {
  roles: [],
  password: null,
  isPasswordRequired: true,
};

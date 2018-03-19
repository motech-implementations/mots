import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import { View } from 'react-native';

import FormField from './FormField';
import { fetchRoles } from '../actions';
import {
  clearFields, getAttributesForInput,
  untouchFields,
} from '../utils/form-utils';
import Button from './Button';
import styles from '../styles/formsStyles';
import Spinner from './Spinner';

export const PROFILE_FORM_NAME = 'ProfileForm';
const FIELDS = {
  name: {
    label: 'Name',
    getAttributes: () => getAttributesForInput(),
  },
  email: {
    label: 'Email',
    getAttributes: () => getAttributesForInput(),
  },
  newPassword: {
    label: 'New Password',
    getAttributes: () => ({
      secureTextEntry: true,
      underlineColorAndroid: 'rgba(0,0,0,0)',
      style: { paddingVertical: 5 },
    }),
  },
  confirmNewPassword: {
    label: 'Confirm New Password',
    getAttributes: () => ({
      secureTextEntry: true,
      underlineColorAndroid: 'rgba(0,0,0,0)',
      style: { paddingVertical: 5 },
    }),
    getDynamicAttributes: ({ newPassword }) => ({
      hidden: !newPassword,
    }),
  },
  password: {
    label: 'Current Password',
    getAttributes: () => ({
      secureTextEntry: true,
      underlineColorAndroid: 'rgba(0,0,0,0)',
      style: { paddingVertical: 5 },
    }),
    getDynamicAttributes: ({ confirmNewPassword }) => ({
      hidden: !confirmNewPassword,
    }),
  },
};

class ProfileForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    clearFields(PROFILE_FORM_NAME, ...(_.keys(FIELDS)));
    untouchFields(PROFILE_FORM_NAME, ...(_.keys(FIELDS)));
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        newPassword={this.props.newPassword}
        confirmNewPassword={this.props.confirmNewPassword}
      />
    );
  }

  renderButton() {
    if (this.props.loading) {
      return (
        <View style={styles.buttonContainer}>
          <Spinner size="small" />
        </View>
      );
    }
    const { handleSubmit } = this.props;

    return (
      <View style={styles.buttonContainer}>
        <Button
          onPress={handleSubmit(this.props.onSubmit)}
          iconName="check"
          iconColor="#FFF"
          buttonColor="#337ab7"
        >
                Submit
        </Button>
        <Button
          onPress={() => this.props.onSubmitCancel()}
          iconName="ban"
          iconColor="#FFF"
          buttonColor="grey"
          marginLeft={10}
        >
                Cancel
        </Button>
      </View>
    );
  }

  render() {
    return (
      <View style={styles.formAddContainer}>
        { _.map(FIELDS, this.renderField) }
        { this.renderButton() }
      </View>
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
  if (values.newPassword && values.confirmNewPassword &&
    values.newPassword !== values.confirmNewPassword) {
    errors.confirmNewPassword = "Password don't match";
  }
  if (values.newPassword && !values.confirmNewPassword) {
    errors.confirmNewPassword = 'Can not be empty';
  }
  if (values.newPassword && values.confirmNewPassword && !values.password) {
    errors.password = 'Can not be empty';
  }
  if (values.email && !emailPattern.test(values.email)) {
    errors.email = 'Email address has incorrect format';
  }

  return errors;
}

const selector = formValueSelector(PROFILE_FORM_NAME);

function mapStateToProps(state) {
  return {
    roles: state.tablesReducer.roles,
    newPassword: selector(state, 'newPassword'),
    confirmNewPassword: selector(state, 'confirmNewPassword'),
  };
}

export default reduxForm({
  validate,
  form: PROFILE_FORM_NAME,
})(connect(mapStateToProps, { fetchRoles })(ProfileForm));

ProfileForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  newPassword: PropTypes.string,
  confirmNewPassword: PropTypes.string,
  loading: PropTypes.bool,
};

ProfileForm.defaultProps = {
  newPassword: null,
  confirmNewPassword: null,
  loading: false,
};

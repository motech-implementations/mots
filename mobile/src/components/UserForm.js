import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import { View } from 'react-native';
import { Select } from 'react-native-chooser';

import FormField from './FormField';
import { fetchRoles } from '../actions';
import {
  clearFields, getAttributesForInput,
  untouchFields,
} from '../utils/form-utils';
import Button from './Button';
import styles from '../styles/formsStyles';
import inputsStyles from '../styles/inputsStyles';
import Spinner from './Spinner';
import commonStyles from '../styles/commonStyles';

const { labelSelectFieldStyle, optionListStyle } = inputsStyles;
const { lightThemeText } = commonStyles;

export const USER_FORM_NAME = 'UserForm';
const FIELDS = {
  username: {
    label: 'Username',
    required: true,
    getAttributes: () => getAttributesForInput(),
  },
  email: {
    label: 'Email',
    getAttributes: () => getAttributesForInput(),
  },
  name: {
    label: 'Name',
    getAttributes: () => getAttributesForInput(),
  },
  roleId: {
    type: Select,
    label: 'Role',
    required: true,
    getSelectOptions: ({ roles }) => ({
      values: roles,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: (input, { roles }) => {
      const role = input && roles ? roles.find(r => r.id === input.value) : {};
      const defaultText = role ? role.name : 'Click to Select';
      return {
        defaultText,
        onSelect: (value) => {
          input.onChange(value);
        },
        transparent: true,
        optionListStyle,
        style: labelSelectFieldStyle,
        textStyle: lightThemeText,
      };
    },
  },
  password: {
    label: 'Password',
    getAttributes: () => ({
      secureTextEntry: true,
      underlineColorAndroid: 'rgba(0,0,0,0)',
      style: { paddingVertical: 5 },
    }),
  },
  passwordConfirm: {
    label: 'Confirm Password',
    getAttributes: () => ({
      secureTextEntry: true,
      underlineColorAndroid: 'rgba(0,0,0,0)',
      style: { paddingVertical: 5 },
    }),
    getDynamicAttributes: ({ password }) => ({
      hidden: !password,
    }),
  },
};

class UserForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    if (this.props.isPasswordRequired) {
      FIELDS.password.required = true;
      FIELDS.passwordConfirm.required = true;
    } else {
      FIELDS.password.required = false;
      FIELDS.passwordConfirm.required = false;
    }
    clearFields(USER_FORM_NAME, ...(_.keys(FIELDS)));
    untouchFields(USER_FORM_NAME, ...(_.keys(FIELDS)));
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
          style={{ marginLeft: 10 }}
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
        {this.renderButton()}
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
  if (values.password && values.passwordConfirm && values.passwordConfirm !== values.password) {
    errors.passwordConfirm = "Password don't match";
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
  loading: PropTypes.bool,
  isPasswordRequired: PropTypes.bool,
};

UserForm.defaultProps = {
  roles: [],
  password: null,
  loading: false,
  isPasswordRequired: true,
};

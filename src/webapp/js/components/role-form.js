import _ from 'lodash';
import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Select from 'react-select';

import FormField from './form-field';
import { fetchPermissions } from '../actions/index';

export const ROLE_FORM_NAME = 'RoleForm';

const FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  permissions: {
    label: 'Permissions',
    type: Select,
    getAttributes: input => ({
      name: input.name,
      value: input.value,
      onChange: (value) => {
        input.onChange(value);
      },
      multi: true,
    }),
    getDynamicAttributes: ({ permissions }) => ({
      options: permissions,
    }),
  },
};

class RoleForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.props.fetchPermissions();
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        permissions={this.props.permissions}
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

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  return errors;
}

function mapStateToProps(state) {
  return {
    permissions: _.map(state.tablesReducer.permissions, val =>
      ({ value: val.id, label: val.displayName })),
  };
}

export default reduxForm({
  validate,
  form: ROLE_FORM_NAME,
})(connect(mapStateToProps, { fetchPermissions })(RoleForm));

RoleForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchPermissions: PropTypes.func.isRequired,
  permissions: PropTypes.arrayOf(PropTypes.shape({})),
};

RoleForm.defaultProps = {
  permissions: [],
};

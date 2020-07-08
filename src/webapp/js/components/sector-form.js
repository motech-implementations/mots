import _ from 'lodash';
import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';
import { fetchLocations } from '../actions/index';

export const SECTOR_FORM_NAME = 'SectorForm';

const FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  districtId: {
    type: 'select',
    label: 'District',
    required: true,
    getSelectOptions: ({ availableLocations }) => ({
      values: availableLocations,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
  },
};

class SectorForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.props.fetchLocations();
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        availableLocations={this.props.availableLocations}
        districtId={this.props.districtId}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
        {_.map(FIELDS, this.renderField)}
        <div className="col-md-2" />
        <button type="submit" className="btn btn-primary margin-bottom-md">Submit</button>
        <button type="button" className="btn btn-danger margin-left-sm margin-bottom-md" onClick={this.props.onSubmitCancel}>Cancel</button>
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
    availableLocations: state.availableLocations,
  };
}

export default reduxForm({
  validate,
  form: SECTOR_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(SectorForm));

SectorForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
};

SectorForm.defaultProps = {
  availableLocations: [],
  districtId: null,
};

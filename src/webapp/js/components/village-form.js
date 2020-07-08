import _ from 'lodash';
import React, { Component } from 'react';
import { formValueSelector, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import FormField from './form-field';
import { fetchLocations } from '../actions/index';
import {
  getAttributesForSelectWithClearOnChange,
  getSelectableLocations,
} from '../utils/form-utils';

export const VILLAGE_FORM_NAME = 'VillageForm';

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
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, VILLAGE_FORM_NAME, 'sectorId', 'facilityId')),
  },
  sectorId: {
    type: 'select',
    label: 'Sector',
    required: true,
    getSelectOptions: ({ availableLocations, districtId }) => ({
      values: getSelectableLocations(
        'sectors',
        availableLocations,
        districtId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, VILLAGE_FORM_NAME, 'facilityId')),
  },
  facilityId: {
    type: 'select',
    label: 'Facility',
    required: true,
    getSelectOptions: ({ availableLocations, districtId, sectorId }) => ({
      values: getSelectableLocations(
        'facilities',
        availableLocations,
        districtId,
        sectorId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
  },
};

class VillageForm extends Component {
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
        sectorId={this.props.sectorId}
        facilityId={this.props.facilityId}
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

const selector = formValueSelector(VILLAGE_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations,
    districtId: selector(state, 'districtId'),
    sectorId: selector(state, 'sectorId'),
    facilityId: selector(state, 'facilityId'),
  };
}

export default reduxForm({
  validate,
  form: VILLAGE_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(VillageForm));

VillageForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
  sectorId: PropTypes.string,
  facilityId: PropTypes.string,
};

VillageForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  sectorId: null,
  facilityId: null,
};

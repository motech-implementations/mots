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

export const FACILITY_FORM_NAME = 'FacilityForm';

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
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, FACILITY_FORM_NAME, 'sectorId')),
  },
  sectorId: {
    type: 'select',
    label: 'Chiefdom',
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
  },
  facilityType: {
    label: 'Facility Type',
    type: 'select',
    getSelectOptions: () => ({
      values: ['CHC', 'CHP', 'MCHP', 'Clinic', 'Hospital'],
    }),
  },
  inchargeFullName: {
    label: 'Incharge name',
  },
  inchargePhone: {
    label: 'Incharge phone',
  },
  inchargeEmail: {
    label: 'Incharge email',
  },
};

class FacilityForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentDidMount() {
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

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  return errors;
}

const selector = formValueSelector(FACILITY_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations,
    districtId: selector(state, 'districtId'),
    sectorId: selector(state, 'sectorId'),
  };
}

export default reduxForm({
  validate,
  form: FACILITY_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(FacilityForm));

FacilityForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
  sectorId: PropTypes.string,
};

FacilityForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  sectorId: null,
};

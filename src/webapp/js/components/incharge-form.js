import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';

import FormField from './form-field';
import { fetchLocations } from '../actions';
import { getAttributesForSelectWithClearOnChange, getSelectableLocations, getAttributesForSelectWithInitializeOnChange } from '../utils/form-utils';

export const INCHARGE_FORM_NAME = 'InchargeForm';

const FIELDS = {
  districtId: {
    type: 'select',
    label: 'District',
    required: true,
    getSelectOptions: ({ availableLocations }) => ({
      values: availableLocations,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, INCHARGE_FORM_NAME, 'chiefdomId', 'facilityId')),
  },
  chiefdomId: {
    type: 'select',
    label: 'Chiefdom',
    required: true,
    getSelectOptions: ({ availableLocations, districtId }) => ({
      values: getSelectableLocations(
        'chiefdoms',
        availableLocations,
        districtId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, INCHARGE_FORM_NAME, 'facilityId')),
  },
  facilityId: {
    type: 'select',
    label: 'Facility',
    required: true,
    getSelectOptions: ({
      availableLocations,
      districtId,
      chiefdomId,
      addIncharge,
    }) => ({
      values: _.map(getSelectableLocations(
        'facilities',
        availableLocations,
        districtId,
        chiefdomId,
      ), (facility) => {
        if (addIncharge && (_.isNull(facility.inchargeId) || facility.inchargeSelected)) {
          return { ...facility, disabled: true };
        } else if (!addIncharge && !_.isNull(facility.inchargeId)) {
          return { ...facility, disabled: true };
        }
        return facility;
      }),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: (input, { addIncharge }) => {
      if (addIncharge) {
        return getAttributesForSelectWithInitializeOnChange(input, INCHARGE_FORM_NAME, '/api/incharge/findByFacilityId');
      }

      return { className: 'form-control', ...input };
    },
  },
  firstName: {
    label: 'First name',
    required: true,
  },
  secondName: {
    label: 'Surname',
    required: true,
  },
  otherName: {
    label: 'Other name',
  },
  phoneNumber: {
    label: 'Phone number',
    required: true,
  },
  email: {
    label: 'Email address',
    getDynamicAttributes: ({ createUser }) => ({
      required: createUser,
    }),
  },
  createUser: {
    getAttributes: input => ({
      ...input,
      type: 'checkbox',
      checked: input.value,
    }),
    getDynamicAttributes: ({ addIncharge }) => ({
      disabled: !addIncharge,
    }),
    label: 'Create user',
  },
};

class InchargeForm extends Component {
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
        dynamicProps={{ addIncharge: this.props.addIncharge }}
        availableLocations={this.props.availableLocations}
        districtId={this.props.districtId}
        chiefdomId={this.props.chiefdomId}
        addIncharge={this.props.addIncharge}
        createUser={this.props.createUser}
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

const selector = formValueSelector(INCHARGE_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations,
    districtId: selector(state, 'districtId'),
    chiefdomId: selector(state, 'chiefdomId'),
    createUser: selector(state, 'createUser'),
  };
}

export default reduxForm({
  validate,
  form: INCHARGE_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(InchargeForm));

InchargeForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
  chiefdomId: PropTypes.string,
  addIncharge: PropTypes.bool,
  createUser: PropTypes.bool,
};

InchargeForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  chiefdomId: null,
  addIncharge: false,
  createUser: false,
};

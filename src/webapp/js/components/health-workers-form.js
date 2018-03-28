import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';
import Select from 'react-select';
import 'react-datetime/css/react-datetime.css';

import FormField from './form-field';
import { fetchLocations } from '../actions/index';
import {
  getAttributesForSelectWithClearOnChange, getSelectableLocations,
  getSupervisorNameFromFacility, fetchDataAndInitializeFrom,
} from '../utils/form-utils';

export const CHW_FORM_NAME = 'HealthWorkersForm';
const FIELDS = {
  chwId: {
    type: Select,
    label: 'CHW Id',
    required: true,
    getAttributes: (input, { addChw, chwId, notSelectedChwIds }) => {
      let options = [{ value: chwId, label: chwId }];

      if (addChw) {
        options = _.map(notSelectedChwIds, chw => ({ value: chw, label: chw }));
      }

      return {
        name: input.name,
        value: input.value,
        onChange: (value) => {
          fetchDataAndInitializeFrom(CHW_FORM_NAME, '/api/chw/findByChwId', value);

          input.onChange(value);
        },
        options,
        simpleValue: true,
      };
    },
    getDynamicAttributes: ({ addChw }) => ({
      disabled: !addChw,
    }),
  },
  firstName: {
    label: 'First Name',
    required: true,
  },
  secondName: {
    label: 'Surname',
    required: true,
  },
  otherName: {
    label: 'Other Name',
  },
  yearOfBirth: {
    label: 'Year of Birth',
    type: DateTime,
    getAttributes: (input) => {
      const dateFormat = 'YYYY';
      const currentYear = DateTime.moment().year();
      const maxYear = (currentYear - 15);

      const yearRange = yearPicker => yearPicker.year() <= maxYear
          && yearPicker.year() >= 1900;

      return {
        dateFormat,
        timeFormat: false,
        isValidDate: yearRange,
        viewDate: maxYear.toString(),
        closeOnSelect: true,
        value: input.value,
        onChange: (param) => {
          let formatted;
          if (typeof param === 'string') {
            formatted = param.length === 0 ? null : param;
          } else {
            formatted = param.format(dateFormat);
          }
          input.onChange(formatted);
        },
      };
    },
  },
  age: {
    label: 'Age',
    getAttributes: () => ({
      disabled: true,
      className: 'form-control',
    }),
    getDynamicAttributes: ({ yearOfBirth }) => ({
      hidden: !yearOfBirth,
      value: Number.isInteger(parseInt(yearOfBirth, 10)) ?
        DateTime.moment().year() - yearOfBirth : '',
    }),
  },
  gender: {
    type: 'select',
    label: 'Gender',
    required: true,
    getSelectOptions: () => ({
      values: ['Male', 'Female'],
    }),
  },
  literacy: {
    type: 'select',
    label: 'Literacy',
    getSelectOptions: () => ({
      values: ['Can read and write', 'Cannot read and write', 'Can only read'],
    }),
  },
  educationLevel: {
    type: 'select',
    label: 'Educational Level',
    getSelectOptions: () => ({
      values: ['Primary', 'Secondary', 'Higher', 'None'],
    }),
  },
  phoneNumber: {
    label: 'Phone Number',
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
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, CHW_FORM_NAME, 'chiefdomId', 'facilityId', 'communityId')),
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
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, CHW_FORM_NAME, 'facilityId', 'communityId')),
  },
  facilityId: {
    type: 'select',
    label: 'Facility',
    required: true,
    getSelectOptions: ({ availableLocations, districtId, chiefdomId }) => ({
      values: getSelectableLocations(
        'facilities',
        availableLocations,
        districtId,
        chiefdomId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, CHW_FORM_NAME, 'communityId', 'supervisorName')),
  },
  supervisorName: {
    label: 'Supervisor',
    getAttributes: () => ({
      disabled: true,
      className: 'form-control',
    }),
    getDynamicAttributes: ({
      availableLocations, districtId, chiefdomId, facilityId,
    }) => {
      if (!facilityId) {
        return { hidden: true };
      }
      const supervisorName =
          getSupervisorNameFromFacility(
            getSelectableLocations(
              'facilities',
              availableLocations,
              districtId,
              chiefdomId,
            ),
            facilityId,
          );

      return { value: supervisorName || 'Unassigned' };
    },
  },
  communityId: {
    type: 'select',
    label: 'Community',
    required: true,
    getSelectOptions: ({
      availableLocations, districtId, chiefdomId, facilityId,
    }) => ({
      values: getSelectableLocations(
        'communities',
        availableLocations,
        districtId,
        chiefdomId,
        facilityId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
  },
  hasPeerSupervisor: {
    getAttributes: input => ({
      ...input,
      type: 'checkbox',
      checked: input.value,
    }),
    label: 'Peer Supervisor',
  },
  preferredLanguage: {
    type: 'select',
    label: 'Preferred Language',
    required: true,
    getSelectOptions: () => ({
      values: ['English', 'Krio', 'Limba', 'Susu', 'Temne', 'Mende'],
    }),
  },
  working: {
    getAttributes: input => ({
      ...input,
      type: 'checkbox',
      checked: input.value,
    }),
    label: 'Working',
  },
};

class HealthWorkersForm extends Component {
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
        dynamicProps={{
          addChw: this.props.addChw,
          chwId: this.props.chwId,
          notSelectedChwIds: this.props.notSelectedChwIds,
        }}
        availableLocations={this.props.availableLocations}
        districtId={this.props.districtId}
        chiefdomId={this.props.chiefdomId}
        facilityId={this.props.facilityId}
        yearOfBirth={this.props.yearOfBirth}
        addChw={this.props.addChw}
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

function isYobBeforeToday(date) {
  return new Date(date) <= new Date();
}

function isAgeLowerThan15(date) {
  const maxValidYear = new Date();
  maxValidYear.setFullYear(maxValidYear.getFullYear() - 15);
  return new Date(date).getYear() <= maxValidYear;
}

function isAgeHigherThan100(date) {
  const minValidYear = new Date();
  minValidYear.setFullYear(minValidYear.getFullYear() - 100);
  return new Date(date).getYear() >= minValidYear;
}

function validate(values) {
  const errors = {};

  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });
  if (values.yearOfBirth && !isYobBeforeToday(values.yearOfBirth)) {
    errors.yearOfBirth = 'Year must be in the past';
  }
  if (values.yearOfBirth && !isAgeLowerThan15(values.yearOfBirth)) {
    errors.yearOfBirth = 'Minimum age is 15';
  }
  if (values.yearOfBirth && !isAgeHigherThan100(values.yearOfBirth)) {
    errors.yearOfBirth = 'Maximum age is 100';
  }

  return errors;
}

const selector = formValueSelector(CHW_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations,
    districtId: selector(state, 'districtId'),
    chiefdomId: selector(state, 'chiefdomId'),
    facilityId: selector(state, 'facilityId'),
    yearOfBirth: selector(state, 'yearOfBirth'),
    chwId: selector(state, 'chwId'),
  };
}

export default reduxForm({
  validate,
  form: CHW_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(HealthWorkersForm));

HealthWorkersForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
  chiefdomId: PropTypes.string,
  facilityId: PropTypes.string,
  yearOfBirth: PropTypes.string,
  chwId: PropTypes.string,
  addChw: PropTypes.bool,
  notSelectedChwIds: PropTypes.arrayOf(PropTypes.string),
};

HealthWorkersForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  chiefdomId: null,
  facilityId: null,
  yearOfBirth: null,
  chwId: '',
  addChw: false,
  notSelectedChwIds: [],
};

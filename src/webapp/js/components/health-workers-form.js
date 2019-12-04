import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import Select from 'react-select';
import 'react-datetime/css/react-datetime.css';

import FormField from './form-field';
import { fetchLocations } from '../actions/index';
import {
  getAttributesForSelectWithClearOnChange, getSelectableLocations,
  fetchDataAndInitializeFrom,
} from '../utils/form-utils';
import apiClient from '../utils/api-client';

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
  familyName: {
    label: 'Family Name',
    required: true,
  },
  gender: {
    type: 'select',
    label: 'Gender',
    required: true,
    getSelectOptions: () => ({
      values: ['Male', 'Female'],
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
    getAttributes: input => (getAttributesForSelectWithClearOnChange(input, CHW_FORM_NAME, 'communityId')),
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
  preferredLanguage: {
    type: 'select',
    label: 'Preferred Language',
    required: true,
    getSelectOptions: () => ({
      values: ['English', 'Krio', 'Limba', 'Susu', 'Temne', 'Mende'],
    }),
  },
  groupId: {
    type: 'select',
    label: 'Group',
    getSelectOptions: ({ groups }) => ({
      values: groups,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
  },
};

class HealthWorkersForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      groups: [],
    };

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.props.fetchLocations();
    this.fetchData();
  }

  fetchData() {
    apiClient.get('/api/group')
      .then((response) => {
        if (response) {
          this.setState({ groups: response.data });
        }
      });
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
        addChw={this.props.addChw}
        groups={this.state.groups}
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

const selector = formValueSelector(CHW_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations,
    districtId: selector(state, 'districtId'),
    chiefdomId: selector(state, 'chiefdomId'),
    facilityId: selector(state, 'facilityId'),
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
  chwId: PropTypes.string,
  addChw: PropTypes.bool,
  notSelectedChwIds: PropTypes.arrayOf(PropTypes.string),
};

HealthWorkersForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  chiefdomId: null,
  facilityId: null,
  chwId: '',
  addChw: false,
  notSelectedChwIds: [],
};

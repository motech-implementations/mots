import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';

import 'react-datetime/css/react-datetime.css';

import FormField from './form-field';
import { createHealthWorker } from '../actions';
import { clearFields, getAttributesForObjectSelect } from '../utils/form-utils';
import apiClient from '../utils/api-client';

export const CHW_FORM_NAME = 'HealthWorkersForm';
const FIELDS = {
  chwId: {
    label: 'CHW Id',
    required: true,
  },
  firstName: {
    label: 'First Name',
  },
  secondName: {
    label: 'Surname',
  },
  otherName: {
    label: 'Other Name',
  },
  dateOfBirth: {
    label: 'Date of Birth',
    type: DateTime,
    getAttributes: (input) => {
      const dateFormat = 'YYYY-MM-DD';

      return {
        dateFormat,
        timeFormat: false,
        closeOnSelect: true,
        value: input.value,
        onChange: (param) => {
          const formatted = !param || typeof param === 'string' ? param : param.format(dateFormat);
          input.onChange(formatted);
        },
      };
    },
  },
  gender: {
    type: 'select',
    label: 'Gender',
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
  district: {
    type: 'select',
    label: 'District',
    getSelectOptions: ({ availableLocations }) => ({
      values: availableLocations,
      displayNameKey: 'name',
    }),
    getAttributes: (input) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          clearFields(CHW_FORM_NAME, 'chiefdom', 'facility', 'communityId');

          input.onChange(attr.onChange(event));
        },
      });
    },
  },
  chiefdom: {
    type: 'select',
    label: 'Chiefdom',
    getSelectOptions: ({ district }) => ({
      values: district && district.chiefdoms,
      displayNameKey: 'name',
    }),
    getAttributes: (input) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          clearFields(CHW_FORM_NAME, 'facility', 'communityId');

          input.onChange(attr.onChange(event));
        },
      });
    },
  },
  facility: {
    type: 'select',
    label: 'Facility',
    getSelectOptions: ({ chiefdom }) => ({
      values: chiefdom && chiefdom.facilities,
      displayNameKey: 'name',
    }),
    getAttributes: (input) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          clearFields(CHW_FORM_NAME, 'communityId');

          input.onChange(attr.onChange(event));
        },
      });
    },
  },
  communityId: {
    type: 'select',
    label: 'Community',
    getSelectOptions: ({ facility }) => ({
      values: facility && facility.communities,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
  },
  hasPeerSupervisor: {
    getAttributes: input => ({
      ...input,
      type: 'checkbox',
      onChange: (event) => {
        const { checked } = event.target;

        if (!checked) {
          clearFields(CHW_FORM_NAME, 'supervisor');
        }

        input.onChange(checked);
      },
    }),
    label: 'Peer Supervisor',
  },
  supervisor: {
    label: 'Supervisor',
    getDynamicAttributes: ({ hasPeerSupervisor }) => ({
      hidden: !hasPeerSupervisor,
    }),
  },
  preferredLanguage: {
    type: 'select',
    label: 'Preferred Language',
    getSelectOptions: () => ({
      values: ['English', 'Krio', 'Limba', 'Susu', 'Temne'],
    }),
  },
};

class HealthWorkersForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      availableLocations: [],
    };

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.fetchLocations();
  }

  fetchLocations() {
    const url = '/api/districts';

    apiClient.get(url).then((response) => {
      const availableLocations = response.data;
      this.setState({ availableLocations });
    });
  }

  renderField(fieldConfig, fieldName) {
    return (
      <FormField
        key={fieldName}
        fieldName={fieldName}
        fieldConfig={fieldConfig}
        availableLocations={this.state.availableLocations}
        district={this.props.district}
        chiefdom={this.props.chiefdom}
        facility={this.props.facility}
        hasPeerSupervisor={this.props.hasPeerSupervisor}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
        { _.map(FIELDS, this.renderField) }
        <div className="col-md-2" />
        <button type="submit" className="btn btn-primary">Submit</button>
        <button className="btn btn-danger" onClick={this.props.onSubmitCancel}>Cancel</button>
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
    district: selector(state, 'district'),
    chiefdom: selector(state, 'chiefdom'),
    facility: selector(state, 'facility'),
    hasPeerSupervisor: selector(state, 'hasPeerSupervisor'),
  };
}

export default reduxForm({
  validate,
  form: CHW_FORM_NAME,
})(connect(mapStateToProps, { createHealthWorker })(HealthWorkersForm));

const facilityPropType = PropTypes.shape({
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  communities: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })),
});

const chiefdomPropType = PropTypes.shape({
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  facilities: PropTypes.arrayOf(facilityPropType),
});

const districtPropType = PropTypes.shape({
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  chiefdoms: PropTypes.arrayOf(chiefdomPropType),
});

HealthWorkersForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  district: districtPropType,
  chiefdom: chiefdomPropType,
  facility: facilityPropType,
  hasPeerSupervisor: PropTypes.bool,
};

HealthWorkersForm.defaultProps = {
  district: null,
  chiefdom: null,
  facility: null,
  hasPeerSupervisor: false,
};

import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm, formValueSelector, change } from 'redux-form';
import { connect } from 'react-redux';
import DateTime from 'react-datetime';
import Alert from 'react-s-alert';
import axios from 'axios';

import 'react-datetime/css/react-datetime.css';

import { createHealthWorker } from '../actions';

const getAttributesForObjectSelect = (input) => {
  const parse = event => (event.target.value ? JSON.parse(event.target.value) : null);

  return {
    className: 'form-control',
    value: JSON.stringify(input.value),
    onBlur: event => input.onBlur(parse(event)),
    onChange: event => input.onChange(parse(event)),
  };
};

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
    getAttributes: (input, { dispatch }) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          dispatch(change('HealthWorkersNewForm', 'chiefdom', null));
          dispatch(change('HealthWorkersNewForm', 'facility', null));
          dispatch(change('HealthWorkersNewForm', 'communityId', null));

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
    getAttributes: (input, { dispatch }) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          dispatch(change('HealthWorkersNewForm', 'facility', null));
          dispatch(change('HealthWorkersNewForm', 'communityId', null));

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
    getAttributes: (input, { dispatch }) => {
      const attr = getAttributesForObjectSelect(input);

      return ({
        ...attr,
        onChange: (event) => {
          dispatch(change('HealthWorkersNewForm', 'communityId', null));

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
    getAttributes: input => getAttributesForObjectSelect(input),
  },
  hasPeerSupervisor: {
    getAttributes: (input, { dispatch }) => ({
      ...input,
      type: 'checkbox',
      onChange: (event) => {
        const { checked } = event.target;

        if (!checked) {
          dispatch(change('HealthWorkersNewForm', 'supervisor', null));
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

class HealthWorkersNew extends Component {
  static renderSelectOptions(options) {
    const { values, valueKey, displayNameKey } = options;

    return [
      <option key="empty" value={null} />,
      _.map(values, (value, index) => {
        const optionValue = JSON.stringify(valueKey ? value[valueKey] : value);
        const displayValue = displayNameKey ? value[displayNameKey] : value;

        return (
          <option key={index} value={optionValue}>
            { displayValue }
          </option>);
      }),
    ];
  }

  constructor(props) {
    super(props);

    this.state = {
      availableLocations: [],
    };

    this.onSubmitCancel = this.onSubmitCancel.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    this.fetchLocations();
  }

  onSubmitCancel() {
    this.props.history.push('/chw');
  }

  onSubmit(values) {
    this.props.createHealthWorker(values, () => {
      Alert.success('CHW has been added');
      this.props.history.push('/chw');
    });
  }

  fetchLocations() {
    const token = localStorage.getItem('token');
    const url = '/api/districts';
    const params = {
      access_token: token,
    };

    axios.get(url, { params }).then((response) => {
      const availableLocations = response.data;
      this.setState({ availableLocations });
    });
  }

  renderInput = ({
    fieldConfig, selectOptions, dynamicAttr, input, meta: { touched, error },
  }) => {
    const { label, type, getAttributes } = fieldConfig;

    const FieldType = type || 'input';
    const attr = getAttributes ? getAttributes(input, this.props) : { type: 'text', className: 'form-control', ...input };
    const attributes = {
      id: input.name,
      disabled: selectOptions && (!selectOptions.values || !selectOptions.values.length),
      ...attr,
      ...dynamicAttr,
    };

    const className = `form-group ${fieldConfig.required ? 'required' : ''} ${attributes.hidden ? 'hidden' : ''} ${touched && error ? 'has-error' : ''}`;

    return (
      <div className={className}>
        <div className="row">
          <label htmlFor={input.name} className="col-md-2 control-label">{ label }</label>
          <div className="col-md-4">
            <FieldType {...attributes}>
              {
                selectOptions && HealthWorkersNew.renderSelectOptions(selectOptions)
              }
            </FieldType>
          </div>
        </div>
        <div className="row">
          <div className="col-md-2" />
          <div className="help-block col-md-4" style={{ float: 'left' }}>
            { touched ? error : '' }
          </div>
        </div>
      </div>
    );
  };

  renderField(fieldConfig, fieldName) {
    return (
      <Field
        fieldConfig={fieldConfig}
        selectOptions={
          fieldConfig.getSelectOptions
            ? fieldConfig.getSelectOptions({ ...this.props, ...this.state })
            : null
        }
        dynamicAttr={
          fieldConfig.getDynamicAttributes ? fieldConfig.getDynamicAttributes(this.props) : {}
        }
        key={fieldName}
        name={fieldName}
        component={this.renderInput}
      />
    );
  }

  render() {
    const { handleSubmit } = this.props;

    return (
      <div>
        <h1 className="page-header">Add Community Health Worker</h1>
        <form className="form-horizontal" onSubmit={handleSubmit(this.onSubmit)}>
          { _.map(FIELDS, this.renderField) }
          <div className="col-md-2" />
          <button type="submit" className="btn btn-primary">Submit</button>
          <button className="btn btn-danger" onClick={this.onSubmitCancel}>Cancel</button>
        </form>
      </div>
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

const selector = formValueSelector('HealthWorkersNewForm');

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
  form: 'HealthWorkersNewForm',
})(connect(mapStateToProps, { createHealthWorker })(HealthWorkersNew));

HealthWorkersNew.propTypes = {
  createHealthWorker: PropTypes.func.isRequired,
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  handleSubmit: PropTypes.func.isRequired,
  district: PropTypes.shape({
    chiefdoms: PropTypes.arrayOf(PropTypes.shape({})),
  }),
  chiefdom: PropTypes.shape({
    facilities: PropTypes.arrayOf(PropTypes.shape({})),
  }),
  facility: PropTypes.shape({
    communities: PropTypes.arrayOf(PropTypes.shape({})),
  }),
  hasPeerSupervisor: PropTypes.bool,
};

HealthWorkersNew.defaultProps = {
  district: null,
  chiefdom: null,
  facility: null,
  hasPeerSupervisor: false,
};

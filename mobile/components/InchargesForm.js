import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import { View } from 'react-native';
import { Select } from 'react-native-chooser';

import FormField from './FormField';
import { fetchLocations } from '../actions';
import {
  clearFields,
  untouchFields,
  getSelectableLocations,
  getAttributesForSelectWithClearOnChange,
} from '../utils/form-utils';
import Button from './Button';
import styles from '../styles/formsStyles';
import Spinner from './Spinner';

export const INCHARGE_FORM_NAME = 'InchargesForm';
const FIELDS = {
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
  phoneNumber: {
    label: 'Phone Number',
    required: true,
  },
  email: {
    label: 'Email address',
  },
  districtId: {
    type: Select,
    label: 'District',
    required: true,
    getSelectOptions: ({ availableLocations }) => ({
      values: availableLocations,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: (input, { availableLocations }) => (
      getAttributesForSelectWithClearOnChange(
        input,
        availableLocations,
        INCHARGE_FORM_NAME,
        'chiefdomId',
        'facilityId',
        'communityId',
      )
    ),
  },
  chiefdomId: {
    type: Select,
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
    getAttributes: (input, { availableLocations, districtId }) => (
      getAttributesForSelectWithClearOnChange(
        input,
        getSelectableLocations(
          'chiefdoms',
          availableLocations,
          districtId,
        ),
        INCHARGE_FORM_NAME,
        'facilityId',
        'communityId',
      )
    ),
    getDynamicAttributes: ({ districtId }) => ({
      hidden: !districtId,
    }),
  },
  facilityId: {
    type: Select,
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
    getAttributes: (input, { availableLocations, districtId, chiefdomId }) => (
      getAttributesForSelectWithClearOnChange(
        input,
        getSelectableLocations(
          'facilities',
          availableLocations,
          districtId,
          chiefdomId,
        ),
        INCHARGE_FORM_NAME,
        'communityId',
      )
    ),
    getDynamicAttributes: ({ chiefdomId }) => ({
      hidden: !chiefdomId,
    }),
  },
};

class InchargesForm extends Component {
  constructor(props) {
    super(props);

    this.renderField = this.renderField.bind(this);
  }

  componentWillMount() {
    clearFields(INCHARGE_FORM_NAME, ...(_.keys(FIELDS)));
    untouchFields(INCHARGE_FORM_NAME, ...(_.keys(FIELDS)));
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
        chiefdomId={this.props.chiefdomId}
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
          marginLeft={10}
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
  };
}

export default reduxForm({
  validate,
  form: INCHARGE_FORM_NAME,
})(connect(mapStateToProps, { fetchLocations })(InchargesForm));

InchargesForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmitCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtId: PropTypes.string,
  chiefdomId: PropTypes.string,
  loading: PropTypes.bool,
};

InchargesForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  chiefdomId: null,
  loading: false,
};

import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import { View, TouchableOpacity, Text } from 'react-native';
import { Select } from 'react-native-chooser';
import Autocomplete from 'react-native-autocomplete-input';

import FormField from './FormField';
import { fetchLocations } from '../actions';
import {
  clearFields,
  untouchFields,
  getSelectableLocations,
  getAttributesForSelect,
  getAttributesForSelectWithClearOnChange,
  getAttributesForInput,
  fetchDataAndInitializeFrom,
} from '../utils/form-utils';
import Button from './Button';
import styles from '../styles/formsStyles';
import Spinner from './Spinner';
import apiClient from '../utils/api-client';

const {
  autocompleteStyle, autocompleteItemStyle, autocompleteHintStyle,
  autocompleteListStyle, autocompleteInputContainerStyle,
} = styles;

export const CHW_FORM_NAME = 'HealthWorkersForm';
const FIELDS = {
  chwId: {
    label: 'CHW Id',
    type: Autocomplete,
    required: true,
    getDynamicAttributes: ({ isChwIdDisabled }) => ({
      editable: !isChwIdDisabled,
    }),
    getAttributes: (input, { notSelectedChwIds }) => {
      const filter = (id) => {
        if (id === '') {
          return notSelectedChwIds;
        }
        const regex = new RegExp(`${id.trim()}`, 'i');
        return notSelectedChwIds.filter(chwId => chwId.search(regex) >= 0);
      };

      const data = filter(input.value);
      const compare = (a, b) => a.toLowerCase().trim() === b.toLowerCase().trim();

      const sortedData = data.length === 1 && compare(input.value, data[0]) ? [] : data;
      const slicedMessage = `Showing first 10 out of ${data.length} results`;
      const slicedData = sortedData.slice(0, 10);
      if (data.length > 10) {
        slicedData.push(slicedMessage);
      }

      return {
        data: slicedData,
        inputContainerStyle: autocompleteInputContainerStyle,
        listStyle: autocompleteListStyle,
        placeholder: 'Select Community Health Worker ID',
        underlineColorAndroid: 'rgba(0,0,0,0)',
        style: autocompleteStyle,
        onChangeText: text => input.onChange(text),
        onBlur: event => input.onBlur(event.target.value),
        onFocus: event => input.onFocus(event.target.value),
        renderItem(item) {
          if (item !== slicedMessage) {
            return (
              <TouchableOpacity
                style={autocompleteItemStyle}
                onPress={() => {
                fetchDataAndInitializeFrom(
                    CHW_FORM_NAME,
                    '/api/chw/findByChwId',
                    item,
                );
                input.onChange(item);
              }}
              >
                <Text>{item}</Text>
              </TouchableOpacity>
            );
          }
          return (
            <Text style={autocompleteHintStyle}>{item}</Text>
          );
        },
      };
    },
  },
  chwName: {
    label: 'CHW Name',
    required: true,
    getAttributes: () => getAttributesForInput(),
  },
  gender: {
    type: Select,
    label: 'Gender',
    getSelectOptions: () => ({
      values: ['Male', 'Female'],
    }),
    getAttributes: input => (getAttributesForSelect(input)),
  },
  phoneNumber: {
    label: 'Phone Number',
    required: true,
    getAttributes: () => getAttributesForInput(),
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
        CHW_FORM_NAME,
        'sectorId',
        'facilityId',
        'villageId',
      )
    ),
  },
  sectorId: {
    type: Select,
    label: 'Sector',
    getSelectOptions: ({ availableLocations, districtId }) => ({
      values: getSelectableLocations(
        'sectors',
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
          'sectors',
          availableLocations,
          districtId,
        ),
        CHW_FORM_NAME,
        'facilityId',
        'villageId',
      )
    ),
    getDynamicAttributes: ({ districtId }) => ({
      hidden: !districtId,
    }),
  },
  facilityId: {
    type: Select,
    label: 'Facility',
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
    getAttributes: (input, { availableLocations, districtId, sectorId }) => (
      getAttributesForSelectWithClearOnChange(
        input,
        getSelectableLocations(
          'facilities',
          availableLocations,
          districtId,
          sectorId,
        ),
        CHW_FORM_NAME,
        'villageId',
      )
    ),
    getDynamicAttributes: ({ sectorId }) => ({
      hidden: !sectorId,
    }),
  },
  villageId: {
    type: Select,
    label: 'Village',
    getSelectOptions: ({
      availableLocations, districtId, sectorId, facilityId,
    }) => ({
      values: getSelectableLocations(
        'villages',
        availableLocations,
        districtId,
        sectorId,
        facilityId,
      ),
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: (input, {
      availableLocations, districtId, sectorId, facilityId,
    }) => (
      getAttributesForSelect(
        input,
        getSelectableLocations(
          'villages',
          availableLocations,
          districtId,
          sectorId,
          facilityId,
        ),
      )
    ),
    getDynamicAttributes: ({ facilityId }) => ({
      hidden: !facilityId,
    }),
  },
  preferredLanguage: {
    type: Select,
    label: 'Preferred Language',
    required: true,
    getSelectOptions: () => ({
      values: ['English', 'Kinyarwanda'],
    }),
    getAttributes: input => (getAttributesForSelect(input)),
  },
  groupId: {
    type: Select,
    label: 'Group',
    getSelectOptions: ({ groups }) => ({
      values: groups,
      displayNameKey: 'name',
      valueKey: 'id',
    }),
    getAttributes: (input, {
      groups,
    }) => (getAttributesForSelect(input, groups)),
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
    clearFields(CHW_FORM_NAME, ...(_.keys(FIELDS)));
    untouchFields(CHW_FORM_NAME, ...(_.keys(FIELDS)));
    this.props.fetchLocations();
    this.fetchData();
  }

  fetchData() {
    apiClient.get('/api/group')
      .then((response) => {
        if (response) {
          this.setState({ groups: response });
        }
      });
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
        isChwIdDisabled={this.props.isChwIdDisabled}
        notSelectedChwIds={this.props.notSelectedChwIds}
        groups={this.state.groups}
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
          style={{ marginLeft: 10 }}
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

function validate(values, props) {
  const errors = {};
  _.each(FIELDS, (fieldConfig, fieldName) => {
    if (fieldConfig.required && !values[fieldName]) {
      errors[fieldName] = 'This field is required';
    }
  });

  if (values.chwId &&
      props.notSelectedChwIds &&
      !props.notSelectedChwIds.includes(values.chwId)) {
    errors.chwId = 'You have to select id from list to populate form';
  }

  return errors;
}

const selector = formValueSelector(CHW_FORM_NAME);

function mapStateToProps(state) {
  return {
    availableLocations: state.availableLocations.districts,
    districtId: selector(state, 'districtId'),
    sectorId: selector(state, 'sectorId'),
    facilityId: selector(state, 'facilityId'),
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
  sectorId: PropTypes.string,
  facilityId: PropTypes.string,
  loading: PropTypes.bool,
  isChwIdDisabled: PropTypes.bool,
  notSelectedChwIds: PropTypes.arrayOf(PropTypes.string),
};

HealthWorkersForm.defaultProps = {
  availableLocations: [],
  districtId: null,
  sectorId: null,
  facilityId: null,
  loading: false,
  isChwIdDisabled: false,
  notSelectedChwIds: [],
};

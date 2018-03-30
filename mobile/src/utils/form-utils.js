import _ from 'lodash';
import { change, initialize, untouch } from 'redux-form';
import { dispatch } from '../App';
import styles from '../styles/inputsStyles';
import commonStyles from '../styles/commonStyles';
import apiClient from './api-client';

const { labelSelectFieldStyle, optionListStyle } = styles;
const { lightThemeText } = commonStyles;

function getLocationById(list, id) {
  if (list) {
    return list.find(location => location.id === id);
  }
  return {};
}

export function getSelectableLocations(
  requestedList, districts,
  districtId, chiefdomId, facilityId,
) {
  let { district, chiefdom, facility } = { district: {}, chiefdom: {}, facility: {} };

  if (districts && districtId) {
    district = getLocationById(districts, districtId);
  }
  if (chiefdomId && district && district.chiefdoms) {
    chiefdom = getLocationById(district.chiefdoms, chiefdomId);
  }
  if (facilityId && chiefdom && chiefdom.facilities) {
    facility = getLocationById(chiefdom.facilities, facilityId);
  }

  switch (requestedList) {
    case 'chiefdoms':
      return district ? district.chiefdoms : [];
    case 'facilities':
      return chiefdom ? chiefdom.facilities : [];
    case 'communities':
      return facility ? facility.communities : [];
    default:
      return [];
  }
}

export function clearFields(formName, ...fields) {
  _.each(fields, (field) => {
    dispatch(change(formName, field, null));
  });
}

export function untouchFields(formName, ...fields) {
  _.each(fields, (field) => {
    dispatch(untouch(formName, field));
  });
}

function initializeForm(formName, formValues) {
  dispatch(initialize(formName, formValues));
}

export function fetchDataAndInitializeFrom(formName, baseUrl, value) {
  if (value && value !== '') {
    const url = `${baseUrl}/${value}`;

    apiClient.get(url)
      .then((response) => {
        initializeForm(formName, response);
      });
  }
}


export function getAttributesForSelect(input, availableLocations) {
  let defaultText = input.value;
  const location = getLocationById(availableLocations, input.value);
  if (input.value && availableLocations && location) {
    defaultText = location.name;
  }

  return {
    defaultText: defaultText || 'Click to Select',
    onSelect: (value) => {
      input.onChange(value);
    },
    transparent: true,
    optionListStyle,
    style: labelSelectFieldStyle,
    textStyle: lightThemeText,
  };
}

export function
getAttributesForSelectWithClearOnChange(input, availableLocations, formName, ...fieldsToClear) {
  let defaultText = input.value;
  const location = getLocationById(availableLocations, input.value);
  if (input.value && availableLocations && location) {
    defaultText = location.name;
  }

  return {
    defaultText: defaultText || 'Click to Select',
    onSelect: (value) => {
      clearFields(formName, ...fieldsToClear);
      input.onChange(value);
    },
    transparent: true,
    optionListStyle,
    style: labelSelectFieldStyle,
    textStyle: lightThemeText,
  };
}

export function
getAttributesForSelectWithInitOnChange(input, availableLocations, formName, baseUrl) {
  let defaultText = input.value;
  const location = getLocationById(availableLocations, input.value);
  if (input.value && availableLocations && location) {
    defaultText = location.name;
  }

  return {
    defaultText: defaultText || 'Click to Select',
    onSelect: (value) => {
      fetchDataAndInitializeFrom(formName, baseUrl, value);
      input.onChange(value);
    },
    transparent: true,
    optionListStyle,
    style: labelSelectFieldStyle,
    textStyle: lightThemeText,
  };
}

export function getAttributesForInput() {
  return {
    underlineColorAndroid: 'rgba(0,0,0,0)',
    style: { paddingVertical: 5, color: '#000' },
  };
}

export function getSupervisorNameFromFacility(list, facilityId) {
  return getLocationById(list, facilityId).inchargeFullName;
}

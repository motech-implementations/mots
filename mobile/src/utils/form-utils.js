import _ from 'lodash';
import { change, initialize, untouch } from 'redux-form';
import { dispatch } from '../App';
import styles from '../styles/inputsStyles';
import commonStyles from '../styles/commonStyles';
import apiClient from './api-client';

const { labelSelectFieldStyle, optionListStyle } = styles;
const { lightThemeText } = commonStyles;

function getItemByKey(list, keyName, value) {
  if (list) {
    return list.find(item => item[keyName] === value);
  }
  return {};
}

function getLocationById(list, id) {
  return getItemByKey(list, 'id', id);
}

export function getSelectableLocations(
  requestedList, districts,
  districtId, sectorId, facilityId,
) {
  let { district, sector, facility } = { district: {}, sector: {}, facility: {} };

  if (districts && districtId) {
    district = getLocationById(districts, districtId);
  }
  if (sectorId && district && district.sectors) {
    sector = getLocationById(district.sectors, sectorId);
  }
  if (facilityId && sector && sector.facilities) {
    facility = getLocationById(sector.facilities, facilityId);
  }

  switch (requestedList) {
    case 'sectors':
      return district ? district.sectors : [];
    case 'facilities':
      return sector ? sector.facilities : [];
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


export function getAttributesForSelect(input, items, valueKey = 'id', displayNameKey = 'name') {
  let defaultText = input.value;
  const item = getItemByKey(items, valueKey, input.value);
  if (input.value && items && item) {
    defaultText = item[displayNameKey];
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

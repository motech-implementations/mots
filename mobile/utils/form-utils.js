import _ from 'lodash';
import { change, untouch } from 'redux-form';
import { dispatch } from '../App';
import styles from '../styles/inputsStyles';

const { labelSelectFieldStyle, optionListStyle } = styles;

function getLocationById(list, id) {
  if (list && id) {
    for (let i = 0; i < list.length; i += 1) {
      if (list[i].id === id) {
        return list[i];
      }
    }
  }
  return {};
}

export function getSelectableLocations(districts, districtId, chiefdomId, facilityId) {
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
  return facility.communities || chiefdom.facilities || district.chiefdoms || districts;
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
  };
}

import _ from 'lodash';
import { change, untouch } from 'redux-form';
import { dispatch } from '../App';
import styles from '../styles/inputsStyles';

const { labelSelectFieldStyle, optionListStyle } = styles;

export function getSelectableLocations(locations, districtId, chiefdomId, facilityId) {
  let { district, chiefdom, facility } = { district: {}, chiefdom: {}, facility: {} };

  if (locations && districtId && locations[districtId]) {
    district = locations[districtId];
  }
  if (chiefdomId && district && district.chiefdoms && district.chiefdoms[chiefdomId]) {
    chiefdom = district.chiefdoms[chiefdomId];
  }
  if (facilityId && chiefdom && chiefdom.facilities && chiefdom.facilities[facilityId]) {
    facility = chiefdom.facilities[facilityId];
  }
  return facility.communities || chiefdom.facilities || district.chiefdoms || locations;
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
  if (input.value && availableLocations && availableLocations[input.value]) {
    defaultText = availableLocations[input.value].name;
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
  if (input.value && availableLocations && availableLocations[input.value]) {
    defaultText = availableLocations[input.value].name;
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

export function sortValuesByName(object) {
  return _.sortBy(_.values(object), x => x.name.toLowerCase());
}

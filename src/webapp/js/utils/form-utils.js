import _ from 'lodash';
import { change } from 'redux-form';
import { dispatch } from '../index';

export function clearFields(formName, ...fields) {
  _.each(fields, (field) => {
    dispatch(change(formName, field, null));
  });
}

export function getAttributesForSelectWithClearOnChange(input, formName, ...fieldsToClear) {
  return {
    className: 'form-control',
    value: input.value,
    onBlur: event => input.onBlur(event.target.value),
    onChange: (event) => {
      clearFields(formName, ...fieldsToClear);

      input.onChange(event.target.value);
    },
  };
}

function getLocationById(list, id) {
  if (list) {
    return list.find(location => location.id === id);
  }
  return {};
}

export function getSupervisorNameFromFacility(list, facilityId) {
  const location = getLocationById(list, facilityId);
  if (location) {
    return location.inchargeFullName;
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
      if (district && district.chiefdoms) return district.chiefdoms;
      break;
    case 'facilities':
      if (chiefdom && chiefdom.facilities) return chiefdom.facilities;
      break;
    case 'communities':
      if (facility && facility.communities) return facility.communities;
      break;
    default:
      return [];
  }

  return [];
}

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

export function getChiefdomsFromDistrictById(districts, districtId) {
  if (districts && districtId) {
    for (let i = 0; i < districts.length; i += 1) {
      if (districts[i].id === districtId) {
        return districts[i].chiefdoms;
      }
    }
  }
  return [];
}

export function getFacilitiesFromChiefdomById(chiefdoms, chiefdomId) {
  if (chiefdoms && chiefdomId) {
    for (let i = 0; i < chiefdoms.length; i += 1) {
      if (chiefdoms[i].id === chiefdomId) {
        return chiefdoms[i].facilities;
      }
    }
  }
  return [];
}

export function getCommunitiesFromFacilitiesById(facilities, facilityId) {
  if (facilities && facilityId) {
    for (let i = 0; i < facilities.length; i += 1) {
      if (facilities[i].id === facilityId) {
        return facilities[i].communities;
      }
    }
  }
  return [];
}

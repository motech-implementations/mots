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

export function getChiefdomsFromDistrictById(list, id) {
  const chiefdoms = [];
  if (list && id) {
    list.forEach((loc) => {
      if (loc.id === id) {
        Array.prototype.push.apply(chiefdoms, loc.chiefdoms);
      }
    });
  }
  return chiefdoms;
}

export function getFacilitiesFromChiefdomById(list, id) {
  const facilities = [];
  if (list && id) {
    list.forEach((loc) => {
      if (loc.id === id) {
        Array.prototype.push.apply(facilities, loc.facilities);
      }
    });
  }
  return facilities;
}

export function getCommunitiesFromFacilitiesById(list, id) {
  const communities = [];
  if (list && id) {
    list.forEach((loc) => {
      if (loc.id === id) {
        Array.prototype.push.apply(communities, loc.communities);
      }
    });
  }
  return communities;
}

import _ from 'lodash';
import { change, initialize } from 'redux-form';
import { dispatch } from '../index';

import apiClient from './api-client';

export function initializeForm(formName, formValues) {
  dispatch(initialize(formName, formValues));
}

export function fetchDataAndInitializeFrom(formName, baseUrl, value) {
  if (value && value !== '') {
    const url = `${baseUrl}/${value}`;

    apiClient.get(url)
      .then((response) => {
        initializeForm(formName, response.data);
      });
  }
}

export function getAttributesForSelectWithInitializeOnChange(input, formName, baseUrl) {
  return {
    className: 'form-control',
    value: input.value,
    onBlur: event => input.onBlur(event.target.value),
    onChange: (event) => {
      const { value } = event.target;

      fetchDataAndInitializeFrom(formName, baseUrl, value);

      input.onChange(value);
    },
  };
}

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
      if (district && district.sectors) return district.sectors;
      break;
    case 'facilities':
      if (sector && sector.facilities) return sector.facilities;
      break;
    case 'villages':
      if (facility && facility.villages) return facility.villages;
      break;
    default:
      return [];
  }

  return [];
}

import _ from 'lodash';
import { change, untouch } from 'redux-form';
import { dispatch } from '../App';
import styles from '../styles/inputsStyles';

const { labelSelectFieldStyle, optionListStyle } = styles;

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

export function getAttributesForSelect(input) {
  return {
    selected: input.value || null,
    onSelect: (value) => {
      input.onChange(value);
    },
    transparent: true,
    optionListStyle,
    style: labelSelectFieldStyle,
  };
}

export function getAttributesForSelectWithClearOnChange(input, formName, ...fieldsToClear) {
  return {
    selected: input.value || null,
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

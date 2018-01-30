import _ from 'lodash';
import { change } from 'redux-form';
import { dispatch } from '../App';

export function clearFields(formName, ...fields) {
  _.each(fields, (field) => {
    dispatch(change(formName, field, null));
  });
}

export function getAttributesForSelectWithClearOnChange(input, formName, ...fieldsToClear) {
  return {
    selected: input.value || null,
    onSelect: (value) => {
      clearFields(formName, ...fieldsToClear);
      input.onChange(value);
    },
  };
}

export function sortValuesByName(object) {
  return _.sortBy(_.values(object), x => x.name.toLowerCase());
}

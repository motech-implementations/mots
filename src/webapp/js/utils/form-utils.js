import _ from 'lodash';
import { change } from 'redux-form';
import { dispatch } from '../index';

export function clearFields(formName, ...fields) {
  _.each(fields, (field) => {
    dispatch(change(formName, field, null));
  });
}

export function getAttributesForObjectSelect(input) {
  const parse = event => (event.target.value ? JSON.parse(event.target.value) : null);

  return {
    className: 'form-control',
    value: JSON.stringify(input.value),
    onBlur: event => input.onBlur(parse(event)),
    onChange: event => input.onChange(parse(event)),
  };
}

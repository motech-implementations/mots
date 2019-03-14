import { FETCH_LOCATIONS } from '../actions/types';

export default function (state = {}, action) {
  switch (action.type) {
    case FETCH_LOCATIONS:
      if (action.payload !== null) {
        return { districts: action.payload } || {};
      }
      return state;
    default:
      return state;
  }
}

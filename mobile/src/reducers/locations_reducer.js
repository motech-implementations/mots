import { FETCH_LOCATIONS } from '../actions/types';

export default function (state = [], action) {
  switch (action.type) {
    case FETCH_LOCATIONS:
      console.log('loc payload', action.payload);
      if (action.payload !== null) {
        return action.payload || [];
      }
      return state;
    default:
      return state;
  }
}

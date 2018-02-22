import {
  FETCH_LOCATIONS,
  FETCH_SELECTABLE_LOCATIONS_FOR_INCHARGES,
} from '../actions/types';

export default function (state = [], action) {
  switch (action.type) {
    case FETCH_LOCATIONS:
    case FETCH_SELECTABLE_LOCATIONS_FOR_INCHARGES:
      if (action.payload.data !== undefined) {
        return action.payload.data;
      }
      return state;
    default:
      return state;
  }
}

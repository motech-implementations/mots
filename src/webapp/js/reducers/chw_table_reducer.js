import { FETCH_CHWS } from '../actions/types';

export default function (state = [], action) {
  switch (action.type) {
    case FETCH_CHWS:
      if (action.payload.data !== undefined) {
        return action.payload.data;
      }
      return state;
    default:
      return state;
  }
}

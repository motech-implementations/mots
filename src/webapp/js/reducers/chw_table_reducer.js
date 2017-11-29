import { FETCH_CHWS } from '../actions/types';

export default function (state = [], action) {
  switch (action.type) {
    case FETCH_CHWS:
      return action.payload.data;
    default:
      return state;
  }
}

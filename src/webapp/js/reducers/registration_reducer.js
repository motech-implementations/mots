import { FETCH_TOKEN } from '../actions/types';

export default function (state = {}, action) {
  switch (action.type) {
    case FETCH_TOKEN:
      if (action.payload !== undefined && action.payload.data) {
        return {
          registrationToken: action.payload.data,
          error: false,
        };
      }
      return {
        error: true,
      };
    default:
      return state;
  }
}

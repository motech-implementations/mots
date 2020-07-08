import { FETCH_REPORTS } from '../actions/types';

export default (state = [], action) => {
  switch (action.type) {
    case FETCH_REPORTS:
      if (action.payload.data !== undefined) {
        return action.payload.data;
      }
      return state;
    default:
      return state;
  }
};

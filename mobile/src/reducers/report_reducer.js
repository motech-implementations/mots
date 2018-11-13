import { FETCH_REPORT } from '../actions/types';

const initialState = { };

export default function (state = initialState, action) {
  switch (action.type) {
    case FETCH_REPORT:
      if (action.payload && action.payload.length) {
        const newState = Object.assign({}, state);
        newState[action.meta.templateId] = action.payload || [];
        return newState;
      }
      return state;
    default:
      return state;
  }
}

import { REHYDRATE } from 'redux-persist/lib/constants';

const initialState = {
  rehydrated: false,
};

export default function (state = initialState, action) {
  switch (action.type) {
    case REHYDRATE:
      return { ...state, rehydrated: true };
    default:
      return state;
  }
}

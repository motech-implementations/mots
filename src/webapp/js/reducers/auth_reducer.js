import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, SET_COUNTER_LOGOUT_TIME, RESET_LOGOUT_COUNTER,
} from '../actions/types';

const initialState = {
  error: null,
  authenticated: false,
  counterLogoutTime: 600,
  resetCounter: false,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case AUTH_USER:
      return { ...state, error: null, authenticated: true };
    case UNAUTH_USER:
      localStorage.removeItem('token');
      localStorage.removeItem('refresh_token');
      return { ...state, authenticated: false };
    case SET_COUNTER_LOGOUT_TIME:
      return { ...state, counterLogoutTime: action.payload, resetCounter: !state.resetCounter };
    case RESET_LOGOUT_COUNTER:
      return { ...state, resetCounter: !state.resetCounter };
    case AUTH_ERROR:
      return { ...state, error: action.payload, authenticated: false };
    default:
      return state;
  }
};

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR } from '../actions/types';

export default function (state = { error: null, authenticated: false }, action) {
  switch (action.type) {
    case AUTH_USER:
      return { ...state, error: null, authenticated: true };
    case UNAUTH_USER:
      localStorage.removeItem('token');
      localStorage.removeItem('refresh_token');
      return { ...state, authenticated: false };
    case AUTH_ERROR:
      return { ...state, error: action.payload, authenticated: false };
    default:
      return state;
  }
}

import { AsyncStorage } from 'react-native';

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR, STORE_LOGIN } from '../actions/types';

const initialState = {
  error: null,
  authenticated: false,
  expirationTime: null,
  savedLogins: {},
};

const OFFLINE_LOGIN_DURATION = 1800;

export default function (state = initialState, action) {
  switch (action.type) {
    case AUTH_USER: {
      const expirationTime = (action.payload) ?
        new Date().getTime() + (OFFLINE_LOGIN_DURATION * 1000) : state.expirationTime;
      return {
        ...state,
        error: null,
        authenticated: true,
        expirationTime,
      };
    }
    case UNAUTH_USER:
      AsyncStorage.removeItem('token');
      AsyncStorage.removeItem('refresh_token');
      return { ...state, authenticated: false, expirationTime: null };
    case AUTH_ERROR:
      return { ...state, error: action.payload, authenticated: false };
    case STORE_LOGIN: {
      const { username, hash, token } = action.payload;
      return {
        ...state,
        savedLogins: {
          ...state.savedLogins,
          [username]: { hash, token },
        },
      };
    }
    default:
      return state;
  }
}

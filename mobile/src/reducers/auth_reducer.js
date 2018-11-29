import { AsyncStorage } from 'react-native';

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR, STORE_LOGIN } from '../actions/types';

const initialState = {
  error: null,
  authenticated: false,
  expirationTime: null,
  savedLogins: {},
};

const OFFLINE_LOGIN_DURATION = 1800;

function clearLoginState() {
  AsyncStorage.removeItem('token');
  AsyncStorage.removeItem('refresh_token');
  return { authenticated: false, expirationTime: null };
}

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
      return { ...state, ...clearLoginState() };
    case AUTH_ERROR:
      return { ...state, ...clearLoginState(), error: action.payload };
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

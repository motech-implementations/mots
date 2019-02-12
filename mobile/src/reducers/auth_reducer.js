import { AsyncStorage } from 'react-native';

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR, STORE_LOGIN } from '../actions/types';

const CLEAR_LOGIN_STATE = {
  authenticated: false,
  expirationTime: null,
  accessToken: null,
  refreshToken: null,
};

const initialState = {
  error: null,
  savedLogins: {},
  ...CLEAR_LOGIN_STATE,
};

const OFFLINE_LOGIN_DURATION = 1800;

function clearTokens() {
  AsyncStorage.removeItem('token');
  AsyncStorage.removeItem('refresh_token');
}

export default function (state = initialState, action) {
  switch (action.type) {
    case AUTH_USER: {
      const { isOffline, accessToken, refreshToken } = action.payload;
      AsyncStorage.setItem('token', accessToken);
      if (refreshToken) {
        AsyncStorage.setItem('refresh_token', refreshToken);
      }
      const expirationTime = (isOffline) ?
        new Date().getTime() + (OFFLINE_LOGIN_DURATION * 1000) : null;
      return {
        ...state,
        error: null,
        authenticated: true,
        expirationTime,
        accessToken,
        refreshToken,
      };
    }
    case UNAUTH_USER:
      clearTokens();
      return { ...state, ...CLEAR_LOGIN_STATE };
    case AUTH_ERROR:
      clearTokens();
      return { ...state, ...CLEAR_LOGIN_STATE, error: action.payload };
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

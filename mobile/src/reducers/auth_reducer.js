import { AsyncStorage } from 'react-native';

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR, STORE_LOGIN } from '../actions/types';

const initialState = {
  error: null,
  authenticated: false,
  expirationDate: null,
  savedLogins: {},
};

export default function (state = initialState, action) {
  switch (action.type) {
    case AUTH_USER: {
      const expirationDate = (action.payload) ?
        new Date(new Date().getTime() + (action.payload * 1000)) : state.expirationDate;
      return {
        ...state,
        error: null,
        authenticated: true,
        expirationDate,
      };
    }
    case UNAUTH_USER:
      AsyncStorage.removeItem('token');
      AsyncStorage.removeItem('refresh_token');
      return { ...state, authenticated: false };
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

import Client from 'client-oauth2';
import { AUTH_USER, UNAUTH_USER, AUTH_ERROR } from './types';

const BASE_URL = '/api';

const auth = new Client({
  clientId: 'trusted-client',
  clientSecret: 'secret',
  accessTokenUri: `${BASE_URL}/oauth/token`
});

export function signinUser({ username, password }, callback) {
  return function (dispatch) {
    auth.owner.getToken(username, password)
    .then(response => {
      dispatch({ type: AUTH_USER });
      localStorage.setItem('token', response.accessToken);
      callback();
    })
    .catch(() => {
      dispatch(authError('Bad Credentials'));
    });
  }
}

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error
  };
}

export function signoutUser() {
  localStorage.removeItem('token');

  return { type: UNAUTH_USER };
}

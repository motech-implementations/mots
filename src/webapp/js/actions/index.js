import Client from 'client-oauth2';
import apiClient from '../utils/api-client';

import { AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER } from './types';

const BASE_URL = '/api';

const auth = new Client({
  clientId: 'trusted-client',
  clientSecret: 'secret',
  accessTokenUri: `${BASE_URL}/oauth/token`,
});

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signinUser({ username, password }, callback) {
  return (dispatch) => {
    auth.owner.getToken(username, password)
      .then((response) => {
        dispatch({ type: AUTH_USER });
        localStorage.setItem('token', response.accessToken);
        callback();
      })
      .catch(() => {
        dispatch(authError('Bad Credentials'));
      });
  };
}

export function signoutUser() {
  localStorage.removeItem('token');

  return { type: UNAUTH_USER };
}

export function fetchChws() {
  const url = `${BASE_URL}/chw`;
  const request = apiClient.get(url);

  return {
    type: FETCH_CHWS,
    payload: request,
  };
}

export function createHealthWorker(values, callback) {
  const request = apiClient.post(`${BASE_URL}/chw`, values);
  request.then(() => callback());

  return {
    type: CREATE_HEALTH_WORKER,
    payload: request,
  };
}

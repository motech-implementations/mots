import axios from 'axios';
import Client from 'client-oauth2';
import apiClient from '../utils/api-client';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES, SAVE_INCHARGE,
} from './types';

const BASE_URL = '/api';
const AUTH_URL = `${BASE_URL}/oauth/token`;

const CLIENT_ID = 'trusted-client';
const CLIENT_SECRET = 'secret';

const authClient = new Client({
  clientId: CLIENT_ID,
  clientSecret: CLIENT_SECRET,
  accessTokenUri: AUTH_URL,
});

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signinUser({ username, password }, callback) {
  return (dispatch) => {
    authClient.owner.getToken(username, password)
      .then((response) => {
        dispatch({ type: AUTH_USER });
        localStorage.setItem('token', response.accessToken);
        localStorage.setItem('refresh_token', response.refreshToken);
        callback();
      })
      .catch(() => {
        dispatch(authError('Bad Credentials'));
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  return dispatch => axios({
    method: 'post',
    url: AUTH_URL,
    auth: {
      username: CLIENT_ID,
      password: CLIENT_SECRET,
    },
    params: {
      grant_type: 'refresh_token',
      refresh_token: refreshToken,
    },
  })
    .catch(() => {
      dispatch(authError('Error occurred when refreshing the user session'));
    })
    .then((response) => {
      localStorage.setItem('token', response.data.access_token);
      localStorage.setItem('refresh_token', response.data.refresh_token);
      dispatch({ type: AUTH_USER });

      if (callback) {
        return callback();
      }

      return null;
    });
}

export function signoutUser() {
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

export function fetchIncharges() {
  const url = `${BASE_URL}/incharge`;
  const request = apiClient.get(url);

  return {
    type: FETCH_INCHARGES,
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

export function saveHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function createIncharge(values, callback) {
  const request = apiClient.post(`${BASE_URL}/incharge`, values);
  request.then(() => callback());

  return {
    type: CREATE_INCHARGE,
    payload: request,
  };
}

export function saveIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_INCHARGE,
    payload: request,
  };
}

export function fetchLocations() {
  const url = '/api/districts';
  const request = apiClient.get(url);

  return {
    type: FETCH_LOCATIONS,
    payload: request,
  };
}

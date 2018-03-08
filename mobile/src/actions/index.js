import { AsyncStorage } from 'react-native';
import apiClient from '../utils/api-client';
import AuthClient from '../utils/auth-client';
import parseJwt from '../utils/encodeUtils';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES, SAVE_INCHARGE,
  SET_COUNTER_LOGOUT_TIME, FETCH_USERS, CREATE_USER, SAVE_USER, FETCH_ROLES,
} from './types';

const BASE_URL = '/api';

const CLIENT_ID = 'trusted-client';
const CLIENT_SECRET = 'secret';

const authClient = new AuthClient({
  clientId: CLIENT_ID,
  clientSecret: CLIENT_SECRET,
});

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signinUser({ username, password }, callback, errorCallback) {
  return (dispatch) => {
    authClient.getToken(username, password)
      .then((response) => {
        response.json().then((data) => {
          dispatch({ type: AUTH_USER });
          const tokenDecoded = parseJwt(data.access_token);
          dispatch({
            type: SET_COUNTER_LOGOUT_TIME,
            payload: tokenDecoded.exp_period,
          });
          AsyncStorage.setItem('token', data.access_token);
          AsyncStorage.setItem('refresh_token', data.refresh_token);
          callback();
        }).catch(() => {
          errorCallback();
        });
      })
      .catch(() => {
        dispatch(authError('Wrong username or password. Please try again.'));
        errorCallback();
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  return (dispatch) => {
    authClient.refreshToken(refreshToken)
      .then(response =>
        response.json())
      .then((data) => {
        dispatch({ type: AUTH_USER });
        const tokenDecoded = parseJwt(data.access_token);
        dispatch({
          type: SET_COUNTER_LOGOUT_TIME,
          payload: tokenDecoded.exp_period,
        });
        AsyncStorage.setItem('token', data.access_token);
        AsyncStorage.setItem('refresh_token', data.refresh_token);
        dispatch({ type: AUTH_USER });

        if (callback) {
          return callback();
        }

        return null;
      })
      .catch(() => {
        dispatch(authError('Error occurred when refreshing token'));
      });
  };
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
  request.then(result => callback(result));

  return {
    type: CREATE_HEALTH_WORKER,
    payload: request,
  };
}

export function saveHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_HEALTH_WORKER,
    payload: request,
  };
}

export function createIncharge(values, callback) {
  const request = apiClient.post(`${BASE_URL}/incharge`, values);
  request.then(result => callback(result));

  return {
    type: CREATE_INCHARGE,
    payload: request,
  };
}

export function saveIncharge(values, callback) {
  const request = apiClient.put(`${BASE_URL}/incharge/${values.id}`, values);
  request.then(result => callback(result));

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

export function fetchRoles() {
  const url = '/api/role';
  const request = apiClient.get(url);

  return {
    type: FETCH_ROLES,
    payload: request,
  };
}

export function fetchUsers() {
  const url = `${BASE_URL}/user`;
  const request = apiClient.get(url);

  return {
    type: FETCH_USERS,
    payload: request,
  };
}

export function createUser(values, callback) {
  const request = apiClient.post(`${BASE_URL}/user`, values);
  request.then(result => callback(result));

  return {
    type: CREATE_USER,
    payload: request,
  };
}

export function saveUser(values, callback) {
  const request = apiClient.put(`${BASE_URL}/user/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_USER,
    payload: request,
  };
}

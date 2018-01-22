import axios from 'axios';
import { AsyncStorage } from 'react-native';
import ApiClient from '../utils/api-client';
import AuthClient from '../utils/auth-client';
import parseJwt from '../utils/encodeUtils';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES, SAVE_INCHARGE,
  SET_COUNTER_LOGOUT_TIME,
} from '../../src/webapp/js/actions/types';

const BASE_URL = '/api';
const AUTH_URL = `${BASE_URL}/oauth/token`;

const CLIENT_ID = 'trusted-client';
const CLIENT_SECRET = 'secret';

const authClient = new AuthClient({
  clientId: CLIENT_ID,
  clientSecret: CLIENT_SECRET,
});

const apiClient = new ApiClient();

export function authError(error) {
  return {
    type: AUTH_ERROR,
    payload: error,
  };
}

export function signinUser({ username, password }, callback) {
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
        });
      })
      .catch(() => {
        dispatch(authError('Bad Credentials'));
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  //TODO rewrite this function using new api client
  return dispatch => axios({
    method: 'post',
    url: `http://10.0.2.2:8080${AUTH_URL}`,
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
      AsyncStorage.setItem('token', response.data.access_token);
      AsyncStorage.setItem('refresh_token', response.data.refresh_token);
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

import axios from 'axios';
import Client from 'client-oauth2';
import jwtDecode from 'jwt-decode';
import apiClient from '../utils/api-client';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES,
  SAVE_INCHARGE,
  SET_COUNTER_LOGOUT_TIME, RESET_LOGOUT_COUNTER, FETCH_USERS, FETCH_CHIEFDOMS,
  FETCH_DISTRICTS, FETCH_FACILITIES, FETCH_COMMUNITIES,
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
        const tokenDecoded = jwtDecode(response.accessToken);
        dispatch({
          type: SET_COUNTER_LOGOUT_TIME,
          payload: tokenDecoded.exp_period,
        });
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

export function resetLogoutCounter() {
  return { type: RESET_LOGOUT_COUNTER };
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

export function fetchUsers() {
  const url = '/api/user';
  const request = apiClient.get(url);

  return {
    type: FETCH_USERS,
    payload: request,
  };
}

export function fetchDistricts() {
  const url = '/api/districtsOnly';
  const request = apiClient.get(url);

  return {
    type: FETCH_DISTRICTS,
    payload: request,
  };
}

export function fetchChiefdoms() {
  const url = '/api/chiefdomsOnly';
  const request = apiClient.get(url);

  return {
    type: FETCH_CHIEFDOMS,
    payload: request,
  };
}

export function fetchFacilities() {
  const url = '/api/facilitiesOnly';
  const request = apiClient.get(url);

  return {
    type: FETCH_FACILITIES,
    payload: request,
  };
}

export function fetchCommunities() {
  const url = '/api/communitiesOnly';
  const request = apiClient.get(url);

  return {
    type: FETCH_COMMUNITIES,
    payload: request,
  };
}

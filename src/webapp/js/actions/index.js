import axios from 'axios';
import Client from 'client-oauth2';
import jwtDecode from 'jwt-decode';
import apiClient from '../utils/api-client';

import {
  AUTH_USER, UNAUTH_USER, AUTH_ERROR, FETCH_CHWS, CREATE_HEALTH_WORKER,
  SAVE_HEALTH_WORKER, FETCH_LOCATIONS, CREATE_INCHARGE, FETCH_INCHARGES,
  SAVE_INCHARGE,
  SET_COUNTER_LOGOUT_TIME, RESET_LOGOUT_COUNTER, FETCH_USERS, FETCH_CHIEFDOMS,
  FETCH_DISTRICTS, FETCH_FACILITIES, FETCH_COMMUNITIES, CREATE_USER,
  FETCH_ROLES, SAVE_USER,
} from './types';
import { dispatch } from '../index';

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
  return (dispatcher) => {
    authClient.owner.getToken(username, password)
      .then((response) => {
        dispatcher({ type: AUTH_USER });
        const tokenDecoded = jwtDecode(response.accessToken);
        dispatcher({
          type: SET_COUNTER_LOGOUT_TIME,
          payload: tokenDecoded.exp_period,
        });
        localStorage.setItem('token', response.accessToken);
        localStorage.setItem('refresh_token', response.refreshToken);
        callback();
      })
      .catch(() => {
        dispatcher(authError('Wrong username or password. Please try again.'));
      });
  };
}

export function useRefreshToken(refreshToken, callback) {
  return dispatcher => axios({
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
      dispatcher(authError('Error occurred when refreshing the user session'));
    })
    .then((response) => {
      localStorage.setItem('token', response.data.access_token);
      localStorage.setItem('refresh_token', response.data.refresh_token);
      dispatcher({ type: AUTH_USER });

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
  const refreshToken = localStorage.getItem('refresh_token');
  const token = localStorage.getItem('token');

  if (token && refreshToken) {
    const currentTime = Date.now() / 1000;
    const decoded = jwtDecode(token);

    if (decoded.exp < currentTime) {
      const refreshDecoded = jwtDecode(refreshToken);

      if (refreshDecoded.exp > currentTime) {
        dispatch(useRefreshToken(refreshToken));
      }
    }
  }

  return { type: RESET_LOGOUT_COUNTER };
}

export function fetchChws(searchParams) {
  const url = `${BASE_URL}/chw/search`;
  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: FETCH_CHWS,
    payload: request,
  };
}

export function fetchIncharges(searchParams) {
  const url = `${BASE_URL}/incharge/search`;
  const request = apiClient.get(url, {
    params: searchParams,
  });

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

export function fetchRoles() {
  const url = '/api/role';
  const request = apiClient.get(url);

  return {
    type: FETCH_ROLES,
    payload: request,
  };
}

export function createUser(values, callback) {
  const request = apiClient.post(`${BASE_URL}/user`, values);
  request.then(() => callback());

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

export function fetchLocationsOfType(type) {
  let url;

  switch (type) {
    case FETCH_DISTRICTS:
      url = '/api/districtsOnly';
      break;
    case FETCH_CHIEFDOMS:
      url = '/api/chiefdomsOnly';
      break;
    case FETCH_FACILITIES:
      url = '/api/facilitiesOnly';
      break;
    case FETCH_COMMUNITIES:
      url = '/api/communitiesOnly';
      break;
    default:
      throw new Error('Unexpected use of fetchLocationsOfType method');
  }
  const request = apiClient.get(url);

  return {
    type,
    payload: request,
  };
}

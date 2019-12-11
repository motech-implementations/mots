/* eslint-disable no-param-reassign */
import axios from 'axios';
import Client from 'client-oauth2';
import jwtDecode from 'jwt-decode';
import apiClient from '../utils/api-client';

import {
  AUTH_USER,
  UNAUTH_USER,
  AUTH_ERROR,
  FETCH_CHWS,
  SAVE_HEALTH_WORKER,
  FETCH_LOCATIONS,
  SAVE_ROLE,
  CREATE_ROLE,
  SET_COUNTER_LOGOUT_TIME,
  RESET_LOGOUT_COUNTER,
  FETCH_USERS,
  FETCH_SECTORS,
  FETCH_DISTRICTS,
  FETCH_FACILITIES,
  FETCH_VILLAGES,
  CREATE_USER,
  FETCH_ROLES,
  SEARCH_ROLES,
  SAVE_USER,
  CREATE_FACILITY,
  CREATE_VILLAGE,
  SAVE_VILLAGE,
  SAVE_FACILITY,
  SAVE_USER_PROFILE,
  FETCH_REPORTS,
  FETCH_PERMISSIONS,
  SAVE_SECTOR,
  CREATE_SECTOR,
  SAVE_DISTRICT,
  CREATE_DISTRICT,
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
        dispatch(authError('Wrong username or password. Please try again.'));
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
  return (dispatch) => {
    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
      const refreshToken = localStorage.getItem('refresh_token');
      dispatch(useRefreshToken(refreshToken));
    }

    dispatch({ type: RESET_LOGOUT_COUNTER });
  };
}

export function fetchChws(searchParams, selected) {
  const url = `${BASE_URL}/chw/search`;

  searchParams.selected = selected;

  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: FETCH_CHWS,
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

export function selectHealthWorker(values, callback) {
  const request = apiClient.put(`${BASE_URL}/chw/${values.id}/select`, values);
  request.then(() => callback());

  return {
    type: SAVE_HEALTH_WORKER,
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

export function fetchUsers(searchParams) {
  const url = '/api/user/search';
  const request = apiClient.get(url, {
    params: searchParams,
  });

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

export function searchRoles(searchParams) {
  const url = '/api/role/search';
  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type: SEARCH_ROLES,
    payload: request,
  };
}

export function fetchPermissions() {
  const url = '/api/permission';
  const request = apiClient.get(url);

  return {
    type: FETCH_PERMISSIONS,
    payload: request,
  };
}

export function createRole(values, callback) {
  const request = apiClient.post(`${BASE_URL}/role`, values);
  request.then(() => callback());

  return {
    type: CREATE_ROLE,
    payload: request,
  };
}

export function saveRole(values, callback) {
  const request = apiClient.put(`${BASE_URL}/role/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_ROLE,
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

export function createFacility(values, callback) {
  const request = apiClient.post(`${BASE_URL}/facility`, values);
  request.then(() => callback());

  return {
    type: CREATE_FACILITY,
    payload: request,
  };
}

export function saveFacility(values, callback) {
  const request = apiClient.put(`${BASE_URL}/facility/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_FACILITY,
    payload: request,
  };
}

export function createVillage(values, callback) {
  const request = apiClient.post(`${BASE_URL}/village`, values);
  request.then(() => callback());

  return {
    type: CREATE_VILLAGE,
    payload: request,
  };
}

export function saveVillage(values, callback) {
  const request = apiClient.put(`${BASE_URL}/village/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_VILLAGE,
    payload: request,
  };
}

export function createSector(values, callback) {
  const request = apiClient.post(`${BASE_URL}/sector`, values);
  request.then(() => callback());

  return {
    type: CREATE_SECTOR,
    payload: request,
  };
}

export function saveSector(values, callback) {
  const request = apiClient.put(`${BASE_URL}/sector/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_SECTOR,
    payload: request,
  };
}

export function createDistrict(values, callback) {
  const request = apiClient.post(`${BASE_URL}/district`, values);
  request.then(() => callback());

  return {
    type: CREATE_DISTRICT,
    payload: request,
  };
}

export function saveDistrict(values, callback) {
  const request = apiClient.put(`${BASE_URL}/district/${values.id}`, values);
  request.then(() => callback());

  return {
    type: SAVE_DISTRICT,
    payload: request,
  };
}

export function saveUserProfile(values, callback) {
  const request = apiClient.put(`${BASE_URL}/user/profile/${values.id}`, values);
  request.then(result => callback(result));

  return {
    type: SAVE_USER_PROFILE,
    payload: request,
  };
}

export function fetchLocationsOfType(type, searchParams) {
  let url;

  switch (type) {
    case FETCH_DISTRICTS:
      url = `${BASE_URL}/district/locations/search`;
      break;
    case FETCH_SECTORS:
      url = `${BASE_URL}/sector/locations/search`;
      break;
    case FETCH_FACILITIES:
      url = `${BASE_URL}/facility/locations/search`;
      break;
    case FETCH_VILLAGES:
      url = `${BASE_URL}/village/locations/search`;
      break;
    default:
      throw new Error('Unexpected use of fetchLocationsOfType method');
  }
  const request = apiClient.get(url, {
    params: searchParams,
  });

  return {
    type,
    payload: request,
  };
}

export function fetchReports() {
  const url = '/api/reports/templates/';
  const request = apiClient.get(url);

  return {
    type: FETCH_REPORTS,
    payload: request,
  };
}
